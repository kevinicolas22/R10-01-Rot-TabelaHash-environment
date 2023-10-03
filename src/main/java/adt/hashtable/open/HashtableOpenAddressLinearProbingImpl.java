package adt.hashtable.open;

import adt.hashtable.hashfunction.HashFunctionClosedAddressMethod;
import adt.hashtable.hashfunction.HashFunctionLinearProbing;
import adt.hashtable.hashfunction.HashFunctionOpenAddress;

public class HashtableOpenAddressLinearProbingImpl<T extends Storable> extends
		AbstractHashtableOpenAddress<T> {

	public HashtableOpenAddressLinearProbingImpl(int size,
												 HashFunctionClosedAddressMethod method) {
		super(size);
		hashFunction = new HashFunctionLinearProbing<T>(size, method);
		this.initiateInternalTable(size);
	}

	@Override
	public void insert(T element) {
		if (isFull()) {
			throw new HashtableOverflowException();
		} else {
			int probe = 0;
			int index = getIndex(element, probe);
			if (element != null) {
				while (this.table[index] != null && this.table[index] != this.deletedElement) {
					probe += 1;
					index = getIndex(element, probe);
					this.COLLISIONS += 1;
				}
				this.table[index] = element;
				this.elements += 1;
			}
		}
	}

	@Override
	public void remove(T element) {
		if (!isEmpty()) {
			int probe = 0;
			int index = getIndex(element, probe);
			if (element != null) {
				while (this.table[index] != null && this.table[index] != this.deletedElement
						&& !this.table[index].equals(element) && probe < this.table.length) {
					probe += 1;
					index = getIndex(element, probe);
				}
				if (this.table[index] != null && this.table[index].equals(element)) {
					this.table[index] = this.deletedElement;
					this.elements -= 1;
				}
			}
		}
	}

	@Override
	public T search(T element) {
		T exit = null;
		int indexof = indexOf(element);
		if (!isEmpty()) {
			if (element != null && indexof > 0) {
				if (this.table[indexof] instanceof DELETED) {
					exit = null;
				} else {
					exit = (T) this.table[indexof];
				}
			}
		}
		return exit;
	}

	@Override
	public int indexOf(T element) {
		int exit = -1;
		if (!isEmpty()) {
			int probe = 0;
			int index = getIndex(element, probe);
			if (element != null) {
				while (this.table[index] != null && this.table[index] != this.deletedElement
						&& !this.table[index].equals(element) && probe < this.table.length) {
					probe += 1;
					index = getIndex(element, probe);
				}
				if (probe < this.table.length) {
					exit = index;
				}
			}
		}
		return exit;
	}

	private int getIndex(T element, int probe) {
		return ((HashFunctionOpenAddress) this.hashFunction).hash(element, probe);
	}

}
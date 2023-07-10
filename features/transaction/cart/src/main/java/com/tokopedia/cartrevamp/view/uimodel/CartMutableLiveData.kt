package com.tokopedia.cartrevamp.view.uimodel

import androidx.lifecycle.LiveData

@Suppress("UNCHECKED_CAST")
class CartMutableLiveData<T>(initialValue: T) : LiveData<T>(initialValue) {

    override fun getValue(): T = super.getValue() as T

    public override fun setValue(value: T) {
        super.setValue(value)
    }
}

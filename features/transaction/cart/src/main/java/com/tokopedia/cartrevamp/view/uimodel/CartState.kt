package com.tokopedia.cartrevamp.view.uimodel

import androidx.lifecycle.LiveData

sealed class CartState<out T : Any> {
    data class ItemLoading(val isLoading: Boolean) : CartState<Nothing>()
    data class ProgressLoading(val isLoading: Boolean) : CartState<Nothing>()
}

sealed class CartGlobalEvent

data class CartEvent<out T : Any>(private val data: T) {
    private var isConsumed: Boolean = false

    fun getData(): T? {
        if (isConsumed) {
            return null
        }
        isConsumed = true
        return data
    }
}

@Suppress("UNCHECKED_CAST")
class CartMutableLiveData<T>(initialValue: T) : LiveData<T>(initialValue) {

    override fun getValue(): T = super.getValue() as T

    public override fun setValue(value: T) {
        super.setValue(value)
    }
}

package com.tokopedia.cartrevamp.view.uimodel

sealed class CartState<out T : Any> {
    object ItemLoading : CartState<Nothing>()
    object ProgressLoading : CartState<Nothing>()
}

sealed class CartGlobalEvent {

}

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

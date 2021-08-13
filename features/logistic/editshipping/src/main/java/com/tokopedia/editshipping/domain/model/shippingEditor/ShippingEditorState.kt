package com.tokopedia.editshipping.domain.model.shippingEditor

sealed class ShippingEditorState<out T: Any> {
    data class Success<out T: Any>(val data:T) : ShippingEditorState<T>()
    data class Fail(val throwable: Throwable?, val errorMessage: String): ShippingEditorState<Nothing>()
    object Loading: ShippingEditorState<Nothing>()
}
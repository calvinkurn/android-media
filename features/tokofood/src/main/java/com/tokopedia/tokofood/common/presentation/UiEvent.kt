package com.tokopedia.tokofood.common.presentation

data class UiEvent(
    var state: Int = 0,
    var data: Any? = null,
    var throwable: Throwable? = null
) {
    companion object {
        const val EVENT_LOADING_DIALOG = 1
        const val EVENT_ERROR_VALIDATE = 2
        const val EVENT_SUCCESS_VALIDATE_CHECKOUT = 3
        const val EVENT_SUCCESS_DELETE_PRODUCT = 4
        const val EVENT_SUCCESS_DELETE_UNAVAILABLE_PRODUCTS = 5
        const val EVENT_SUCCESS_UPDATE_NOTES = 6
        const val EVENT_SUCCESS_UPDATE_QUANTITY = 7
        const val EVENT_SUCCESS_LOAD_CART = 8
    }
}
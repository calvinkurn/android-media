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
        const val EVENT_SUCCESS_UPDATE_CART = 8
        const val EVENT_SUCCESS_LOAD_CART = 9
        const val EVENT_SUCCESS_ADD_TO_CART = 10
        const val EVENT_PHONE_VERIFICATION = 11
        const val EVENT_FAILED_LOAD_CART = 12
    }
}
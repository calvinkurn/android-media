package com.tokopedia.tokofood.common.presentation

data class UiEvent(
    var state: Int = 0,
    var source: String = "",
    var data: Any? = null,
    var throwable: Throwable? = null
) {
    companion object {
        const val EVENT_LOADING_DIALOG = 1
        const val EVENT_SUCCESS_VALIDATE_CHECKOUT = 3
        const val EVENT_SUCCESS_DELETE_PRODUCT = 41
        const val EVENT_SUCCESS_DELETE_UNAVAILABLE_PRODUCTS = 5
        const val EVENT_SUCCESS_UPDATE_NOTES = 6
        const val EVENT_SUCCESS_UPDATE_QUANTITY = 7
        const val EVENT_SUCCESS_UPDATE_QUANTITY_NEW = 71
        const val EVENT_SUCCESS_UPDATE_CART = 8
        const val EVENT_SUCCESS_LOAD_CART = 9
        const val EVENT_SUCCESS_ADD_TO_CART = 10
        const val EVENT_HIDE_LOADING_ADD_TO_CART = 11
        const val EVENT_HIDE_LOADING_UPDATE_TO_CART = 12
        const val EVENT_PHONE_VERIFICATION = 13
        const val EVENT_FAILED_LOAD_CART = 14
        const val EVENT_FAILED_DELETE_PRODUCT = 15
        const val EVENT_FAILED_UPDATE_NOTES = 16
        const val EVENT_FAILED_UPDATE_QUANTITY = 17
        const val EVENT_FAILED_UPDATE_CART = 18
        const val EVENT_FAILED_ADD_TO_CART = 19
    }
}

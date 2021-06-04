package com.tokopedia.minicart.common.widget

data class GlobalEvent(
        var observer: Int = 0,
        var state: Int = 0,
        var data: Any? = null
) {
    companion object {
        const val OBSERVER_MINI_CART_WIDGET = 1
        const val OBSERVER_MINI_CART_LIST_BOTTOM_SHEET = 2

        const val STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET = 1
        const val STATE_SUCCESS_DELETE_CART_ITEM = 10
        const val STATE_SUCCESS_DELETE_ALL_AVAILABLE_CART_ITEM = 11
        const val STATE_FAILED_DELETE_CART_ITEM = 12
        const val STATE_SUCCESS_BULK_DELETE_UNAVAILABLE_ITEMS = 13
        const val STATE_FAILED_BULK_DELETE_UNAVAILABLE_ITEMS = 14
        const val STATE_SUCCESS_UPDATE_CART_FOR_CHECKOUT = 20
        const val STATE_FAILED_UPDATE_CART_FOR_CHECKOUT = 21
    }
}
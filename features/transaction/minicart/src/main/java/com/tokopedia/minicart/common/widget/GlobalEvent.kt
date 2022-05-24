package com.tokopedia.minicart.common.widget

data class GlobalEvent(
        var observer: Int = 0,
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val OBSERVER_MINI_CART_WIDGET = 1
        const val OBSERVER_MINI_CART_LIST_BOTTOM_SHEET = 2
        const val OBSERVER_MINI_CART_GENERAL_WIDGET = 3

        const val STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET = 11
        const val STATE_FAILED_LOAD_MINI_CART_SIMPLIFIED_SUMMARY_BOTTOM_SHEET = 12
        const val STATE_FAILED_LOAD_MINI_CART_CHAT_BOTTOM_SHEET = 13
        const val STATE_SUCCESS_DELETE_CART_ITEM = 22
        const val STATE_FAILED_DELETE_CART_ITEM = 33
        const val STATE_SUCCESS_UNDO_DELETE_CART_ITEM = 44
        const val STATE_FAILED_UNDO_DELETE_CART_ITEM = 55
        const val STATE_SUCCESS_TO_CHECKOUT = 66
        const val STATE_FAILED_TO_CHECKOUT = 77
    }
}
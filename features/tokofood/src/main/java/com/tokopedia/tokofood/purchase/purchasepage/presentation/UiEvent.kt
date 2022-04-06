package com.tokopedia.tokofood.purchase.purchasepage.presentation

data class UiEvent(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val EVENT_SUCCESS_LOAD_PURCHASE_PAGE = 11
        const val EVENT_FAILED_LOAD_PURCHASE_PAGE = 22
        const val EVENT_REMOVE_ALL_PRODUCT = 33
        const val EVENT_SUCCESS_REMOVE_PRODUCT = 44
        const val EVENT_SCROLL_TO_UNAVAILABLE_ITEMS = 55
        const val EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG = 66
        const val EVENT_NAVIGATE_TO_SET_PINPOINT = 77
        const val EVENT_SUCCESS_EDIT_PINPOINT = 88
        const val EVENT_FAILED_EDIT_PINPOINT = 89
    }
}
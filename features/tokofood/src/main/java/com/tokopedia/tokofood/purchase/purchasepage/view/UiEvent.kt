package com.tokopedia.tokofood.purchase.purchasepage.view

data class UiEvent(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val EVENT_REMOVE_ALL_PRODUCT = 10
        const val EVENT_SUCCESS_REMOVE_PRODUCT = 11
        const val EVENT_SCROLL_TO_UNAVAILABLE_ITEMS = 12
        const val EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG = 13
        const val EVENT_NAVIGATE_TO_SET_PINPOINT = 14
    }
}
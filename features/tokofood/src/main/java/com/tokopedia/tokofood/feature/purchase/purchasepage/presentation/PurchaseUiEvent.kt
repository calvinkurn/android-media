package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation

data class PurchaseUiEvent(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val EVENT_SUCCESS_LOAD_PURCHASE_PAGE = 11
        const val EVENT_NO_PINPOINT = 12
        const val EVENT_FAILED_LOAD_PURCHASE_PAGE_PARTIAL = 22
        const val EVENT_FAILED_LOAD_PURCHASE_PAGE = 23
        const val EVENT_EMPTY_PRODUCTS = 33
        const val EVENT_SUCCESS_REMOVE_PRODUCT = 44
        const val EVENT_SCROLL_TO_UNAVAILABLE_ITEMS = 55
        const val EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG = 66
        const val EVENT_NAVIGATE_TO_SET_PINPOINT = 77
        const val EVENT_SUCCESS_EDIT_PINPOINT = 88
        const val EVENT_FAILED_EDIT_PINPOINT = 89
        const val EVENT_SUCCESS_GET_CONSENT = 90
        const val EVENT_SUCCESS_VALIDATE_CONSENT = 91
        const val EVENT_SUCCESS_CHECKOUT_GENERAL = 93
        const val EVENT_FAILED_CHECKOUT_GENERAL_BOTTOMSHEET = 94
        const val EVENT_FAILED_CHECKOUT_GENERAL_TOASTER = 95
        const val EVENT_GO_TO_ORDER_CUSTOMIZATION = 97
    }
}

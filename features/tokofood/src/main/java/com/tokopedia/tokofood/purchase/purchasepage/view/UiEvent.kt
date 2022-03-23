package com.tokopedia.tokofood.purchase.purchasepage.view

data class UiEvent(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val STATE_REMOVE_ALL_PRODUCT = 10
        const val STATE_SCROLL_TO_UNAVAILABLE_ITEMS = 11
    }
}
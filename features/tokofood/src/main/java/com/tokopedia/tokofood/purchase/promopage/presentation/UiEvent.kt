package com.tokopedia.tokofood.purchase.promopage.presentation

data class UiEvent(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val EVENT_SUCCESS_LOAD_PROMO_PAGE = 11
        const val EVENT_FAILED_LOAD_PROMO_PAGE = 22

    }
}
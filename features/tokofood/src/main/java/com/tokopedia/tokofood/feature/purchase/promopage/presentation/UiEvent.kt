package com.tokopedia.tokofood.feature.purchase.promopage.presentation

data class UiEvent(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val EVENT_SUCCESS_LOAD_PROMO_PAGE = 111
        const val EVENT_FAILED_LOAD_PROMO_PAGE = 222
        const val EVENT_ERROR_PAGE_PROMO_PAGE = 333
        const val EVENT_SHOW_TOASTER = 444
        const val EVENT_NO_COUPON = 555
    }
}
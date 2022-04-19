package com.tokopedia.tokofood.purchase.promopage.presentation

data class UiEvent(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val EVENT_SUCCESS_LOAD_PROMO_PAGE = 111
        const val EVENT_FAILED_LOAD_PROMO_PAGE = 222
        const val EVENT_RENDER_GLOBAL_ERROR_KYC = 333
        const val EVENT_RENDER_GLOBAL_ERROR_PROMO_INELIGIBLE = 444
    }
}
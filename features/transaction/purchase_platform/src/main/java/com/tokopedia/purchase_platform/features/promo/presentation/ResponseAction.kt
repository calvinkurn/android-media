package com.tokopedia.purchase_platform.features.promo.presentation

data class ClearPromoResponseAction(
        var state: Int = 0,
        var exception: Throwable? = null
) {
    companion object {
        val ACTION_STATE_SUCCESS = 1
        val ACTION_STATE_ERROR = 2
    }
}

data class ApplyPromoResponseAction(
        var state: Int = 0,
        var exception: Throwable? = null
) {
    companion object {
        val ACTION_NAVIGATE_TO_CART = 1
        val ACTION_SHOW_TOAST_ERROR = 2
        val ACTION_RELOAD_PROMO = 3
    }
}

data class GetCouponRecommendationAction(
        var state: Int = 0
) {
    companion object {
        val ACTION_CLEAR_DATA = 1
    }
}
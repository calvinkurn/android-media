package com.tokopedia.purchase_platform.features.promo.presentation.viewmodel

import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ValidateUsePromoRevampUiModel

data class ClearPromoResponseAction(
        var state: Int = 0,
        var exception: Throwable? = null,
        var data: String? = null,
        var lastValidateUseRequest: ValidateUsePromoRequest? = null
) {
    companion object {
        val ACTION_STATE_SUCCESS = 1
        val ACTION_STATE_ERROR = 2
    }
}

data class ApplyPromoResponseAction(
        var state: Int = 0,
        var exception: Throwable? = null,
        var data: ValidateUsePromoRevampUiModel? = null,
        var lastValidateUseRequest: ValidateUsePromoRequest? = null
) {
    companion object {
        val ACTION_NAVIGATE_TO_CART = 1
        val ACTION_SHOW_TOAST_ERROR = 2
        val ACTION_RELOAD_PROMO = 3
    }
}

data class GetCouponRecommendationAction(
        var state: Int = 0,
        var exception: Throwable? = null
) {
    companion object {
        val ACTION_CLEAR_DATA = 1
        val ACTION_SHOW_TOAST_ERROR =2
    }
}
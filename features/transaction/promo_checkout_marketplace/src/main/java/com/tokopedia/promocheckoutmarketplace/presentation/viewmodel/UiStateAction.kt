package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoSuggestionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel

data class ClearPromoResponseAction(
    var state: Int = 0,
    var exception: Throwable? = null,
    var data: ClearPromoUiModel? = null,
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
        val ACTION_NAVIGATE_TO_CALLER_PAGE = 1
        val ACTION_SHOW_TOAST_ERROR = 2
        val ACTION_SHOW_TOAST_AND_RELOAD_PROMO = 3
    }
}

data class GetPromoListResponseAction(
    var state: Int = 0,
    var exception: Throwable? = null
) {
    companion object {
        val ACTION_CLEAR_DATA = 1
        val ACTION_SHOW_TOAST_ERROR = 2
    }
}

data class GetPromoSuggestionAction(
    var state: Int = 0,
    var data: PromoSuggestionUiModel? = null
) {
    companion object {
        val ACTION_SHOW = 1
        val ACTION_RELEASE_LOCK_FLAG = 2
    }
}

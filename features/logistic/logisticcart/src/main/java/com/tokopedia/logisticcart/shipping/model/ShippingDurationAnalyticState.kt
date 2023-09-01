package com.tokopedia.logisticcart.shipping.model

sealed interface ShippingDurationAnalyticState {
    data class AnalyticCourierPromo(val shippingDurationUiModelList: List<ShippingDurationUiModel>) :
        ShippingDurationAnalyticState

    data class AnalyticPromoLogistic(val promoViewModelList: List<LogisticPromoUiModel>) :
        ShippingDurationAnalyticState
}

package com.tokopedia.logisticcart.shipping.model

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData

sealed interface ChooseShippingDurationState {
    data class NormalShipping(
        val shippingCourierUiModelList: List<ShippingCourierUiModel>,
        val courierData: ShippingCourierUiModel?,
        val cartPosition: Int,
        val selectedServiceId: Int,
        val serviceData: ServiceData,
        val flagNeedToSetPinpoint: Boolean
    ) : ChooseShippingDurationState

    data class FreeShipping(
        val shippingCourierViewModelList: List<ShippingCourierUiModel>,
        val courierData: ShippingCourierUiModel,
        val serviceData: ServiceData,
        val needToSetPinpoint: Boolean,
        val promoCode: String,
        val serviceId: Int,
        val data: LogisticPromoUiModel
    ) : ChooseShippingDurationState

    object CourierNotAvailable : ChooseShippingDurationState
}

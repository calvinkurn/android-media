package com.tokopedia.logisticcart.shipping.features.shippingdurationocc

import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData

interface ShippingDurationOccBottomSheetListener {

    fun onDurationChosen(serviceData: ServiceData, selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean)

    fun onLogisticPromoClicked(data: LogisticPromoUiModel)
}
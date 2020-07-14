package com.tokopedia.logisticcart.shipping.features.shippingcourierocc

import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel

interface ShippingCourierOccBottomSheetListener {

    fun onCourierChosen(shippingCourierViewModel: ShippingCourierUiModel)

    fun onLogisticPromoClicked(data: LogisticPromoUiModel)
}
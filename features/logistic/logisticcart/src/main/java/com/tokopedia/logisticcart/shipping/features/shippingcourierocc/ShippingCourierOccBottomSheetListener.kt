package com.tokopedia.logisticcart.shipping.features.shippingcourierocc

import com.tokopedia.logisticcart.shipping.model.LogisticPromoViewModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel

interface ShippingCourierOccBottomSheetListener {

    fun onCourierChosen(shippingCourierViewModel: ShippingCourierViewModel)

    fun onLogisticPromoClicked(data: LogisticPromoViewModel)
}
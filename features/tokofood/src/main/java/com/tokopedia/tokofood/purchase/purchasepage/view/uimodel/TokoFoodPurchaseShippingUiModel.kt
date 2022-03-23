package com.tokopedia.tokofood.purchase.purchasepage.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.view.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseShippingUiModel(
        var shippingCourierName: String = "",
        var shippingEta: String = "",
        var shippingLogoUrl: String = "",
        var shippingPrice: Long = 0L,
        var wrappingFee: Long = 0L,
        var serviceFee: Long = 0L,
        var isNeedPinpoint: Boolean = false,
        var isShippingUnavailable: Boolean = false
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, BaseUiModel() {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseShippingTokoFoodPurchaseUiModel(
        var shippingCourierName: String = "",
        var shippingEta: String = "",
        var shippingLogoUrl: String = "",
        var shippingPrice: Long = 0L,
        var wrappingFee: Long = 0L,
        var serviceFee: Long = 0L,
        var isNeedPinpoint: Boolean = false,
        var isShippingUnavailable: Boolean = false
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, BaseTokoFoodPurchaseUiModel() {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
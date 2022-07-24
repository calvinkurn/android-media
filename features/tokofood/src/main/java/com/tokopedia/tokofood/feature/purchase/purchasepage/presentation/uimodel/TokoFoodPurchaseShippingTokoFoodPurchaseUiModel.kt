package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseShippingTokoFoodPurchaseUiModel(
        var shippingCourierName: String = "",
        var shippingEta: String = "",
        var shippingLogoUrl: String = "",
        var shippingPriceFmt: String = "",
        var isNeedPinpoint: Boolean = false,
        var isShippingAvailable: Boolean = true
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, CanLoadPartially, BaseTokoFoodPurchaseUiModel() {

    var isLoading = false

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun copyWithLoading(isLoading: Boolean): CanLoadPartially {
        return this.copy().apply {
            this.isLoading = isLoading
        }
    }

}
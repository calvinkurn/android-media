package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel(
        var totalAmount: Double = 0.0,
        var isButtonLoading: Boolean
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, CanLoadPartially, BaseTokoFoodPurchaseUiModel() {

    var isLoading = false

    override fun copyWithLoading(isLoading: Boolean): CanLoadPartially {
        return this.copy().apply {
            this.isLoading = isLoading
        }
    }

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}

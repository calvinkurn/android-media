package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchasePromoTokoFoodPurchaseUiModel(
        var title: String = "",
        var description: String = "",
        var benefitList: List<PromoBenefit> = listOf()
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

    data class PromoBenefit(
        var title: String = "",
        var value: Double = 0.0
    )

}
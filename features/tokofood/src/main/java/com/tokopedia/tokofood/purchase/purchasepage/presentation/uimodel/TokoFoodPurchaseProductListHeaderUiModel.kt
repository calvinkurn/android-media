package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseProductListHeaderUiModel(
        var title: String = "",
        var action: String = "",
        var state: Int = STATE_AVAILABLE
) : Visitable<TokoFoodPurchaseAdapterTypeFactory> {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val STATE_AVAILABLE = 1
        const val STATE_UNAVAILABLE = 2
    }

}
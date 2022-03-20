package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseGeneralTickerUiModel(
        var message: String = ""
) : Visitable<TokoFoodPurchaseAdapterTypeFactory> {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
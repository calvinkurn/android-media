package com.tokopedia.tokofood.purchase.purchasepage.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.view.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseDividerUiModel(
        var height: Int = 0
) : Visitable<TokoFoodPurchaseAdapterTypeFactory> {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
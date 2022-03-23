package com.tokopedia.tokofood.purchase.purchasepage.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.view.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseTotalAmountUiModel(
        var totalAmount: Long = 0L
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, BaseUiModel() {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
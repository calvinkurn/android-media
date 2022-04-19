package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel(
        var title: String = "",
        var action: String = "",
        var isUnavailableHeader: Boolean = false
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, BaseTokoFoodPurchaseUiModel() {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
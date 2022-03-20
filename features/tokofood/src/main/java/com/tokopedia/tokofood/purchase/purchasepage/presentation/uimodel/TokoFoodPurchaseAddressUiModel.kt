package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseAddressUiModel(
        var addressName: String = "",
        var receiverName: String = "",
        var receiverPhone: String = "",
        var addressDetail: String = "",
        var isMainAddress: String = ""
) : Visitable<TokoFoodPurchaseAdapterTypeFactory> {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
package com.tokopedia.tokofood.purchase.purchasepage.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.view.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseAddressUiModel(
        var addressName: String = "",
        var receiverName: String = "",
        var receiverPhone: String = "",
        var addressDetail: String = "",
        var cityName: String = "",
        var districtName: String = "",
        var isMainAddress: Boolean = false
) : Visitable<TokoFoodPurchaseAdapterTypeFactory> {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseAddressTokoFoodPurchaseUiModel(
        var addressName: String = "",
        var receiverName: String = "",
        var receiverPhone: String = "",
        var addressDetail: String = "",
        var cityName: String = "",
        var districtName: String = "",
        var isMainAddress: Boolean = false
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, BaseTokoFoodPurchaseUiModel() {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
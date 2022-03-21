package com.tokopedia.tokofood.purchase.purchasepage.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.view.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseProductUiModel(
        var isDisabled: Boolean = false,
        var name: String = "",
        var imageUrl: String = "",
        var addOns: List<String> = emptyList(),
        var originalPrice: Long = 0L,
        var price: Long = 0L,
        var discountPercentage: String = "",
        var notes: String = "",
        var quantity: Int = 0
) : Visitable<TokoFoodPurchaseAdapterTypeFactory> {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
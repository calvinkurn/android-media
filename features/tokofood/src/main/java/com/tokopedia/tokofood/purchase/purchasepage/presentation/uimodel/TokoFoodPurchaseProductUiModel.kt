package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseProductUiModel(
        var isUnavailable: Boolean = false,
        var id: String = "",
        var name: String = "",
        var imageUrl: String = "",
        var addOns: List<String> = emptyList(),
        var hasAddOnsOption: Boolean = false,
        var originalPrice: Long = 0L,
        var price: Long = 0L,
        var discountPercentage: String = "",
        var notes: String = "",
        var quantity: Int = 0,
        var minQuantity: Int = 0,
        var maxQuantity: Int = 0
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, BaseUiModel() {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
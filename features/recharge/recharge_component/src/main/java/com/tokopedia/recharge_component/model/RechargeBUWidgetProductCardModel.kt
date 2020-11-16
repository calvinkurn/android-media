package com.tokopedia.recharge_component.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recharge_component.presentation.adapter.RechargeBUWidgetProductCardTypeFactory

class RechargeBUWidgetProductCardModel(
        val imageUrl: String,
        val backgroundTintColor: String,
        val imageType: String,
        val categoryName: String,
        val categoryNameColor: String,
        val productName: String,
        val priceLabel: String,
        val discountPercentage: String,
        val slashedPrice: String,
        val price: String
): Visitable<RechargeBUWidgetProductCardTypeFactory> {
    override fun type(typeFactory: RechargeBUWidgetProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
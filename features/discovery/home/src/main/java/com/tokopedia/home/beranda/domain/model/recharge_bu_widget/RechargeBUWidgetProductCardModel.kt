package com.tokopedia.home.beranda.domain.model.recharge_bu_widget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.RechargeBUWidgetProductCardTypeFactory

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
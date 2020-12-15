package com.tokopedia.recharge_component.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recharge_component.presentation.adapter.RechargeBUWidgetProductCardTypeFactory

class RechargeBUWidgetProductCardModel(
        val imageUrl: String,
        val backgroundColor: String,
        val imageType: String,
        val categoryName: String,
        val categoryNameColor: String,
        val productName: String,
        val price: String,
        val slashedPrice: String,
        val discountPercentage: String,
        val applink: String,
        val listener: CommonProductCardCarouselListener
): ImpressHolder(), Visitable<RechargeBUWidgetProductCardTypeFactory> {
    override fun type(typeFactory: RechargeBUWidgetProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.v2.BlankSpaceConfig

class CarouselFeaturedShopCardDataModel (
        val grid: ChannelGrid,
        val impressHolder: ImpressHolder = ImpressHolder(),
        val applink: String = "",
        val componentName: String = "",
        val listener: CommonProductCardCarouselListener
): Visitable<CommonCarouselProductCardTypeFactory>{
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener

class CarouselSeeMorePdpDataModel (
        val applink: String = "",
        val backgroundImage: String = "",
        val listener: CommonProductCardCarouselListener
): Visitable<CommonCarouselProductCardTypeFactory>{
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.model.ChannelModel

data class CarouselEmptyCardDataModel (
     val channel: ChannelModel,
     val parentPosition: Int,
     val listener: CommonProductCardCarouselListener,
     val applink: String = ""
): Visitable<CommonCarouselProductCardTypeFactory>{
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
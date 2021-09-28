package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelViewAllCard
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

/**
 * created by Dhaba
 */
class CarouselViewAllCardDataModel(
    val applink: String = "",
    val channelViewAllCard: ChannelViewAllCard = ChannelViewAllCard(),
    val listener: CommonProductCardCarouselListener,
    val imageUrl: String = "",
    val gradientColor: ArrayList<String> = arrayListOf(""),
    val layoutType: String = ""
) : Visitable<CommonCarouselProductCardTypeFactory> {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
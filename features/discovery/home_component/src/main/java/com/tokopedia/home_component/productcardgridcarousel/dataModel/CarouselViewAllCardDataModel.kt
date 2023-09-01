package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelViewAllCard
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselDiffUtil
import com.tokopedia.unifycomponents.CardUnify2

/**
 * created by Dhaba
 */
class CarouselViewAllCardDataModel(
    val applink: String = "",
    val channelViewAllCard: ChannelViewAllCard = ChannelViewAllCard(),
    val listener: CommonProductCardCarouselListener? = null,
    val imageUrl: String = "",
    val gradientColor: ArrayList<String> = arrayListOf(""),
    val layoutType: String = "",
    val trackingAttributionModel: TrackingAttributionModel = TrackingAttributionModel(),
    val animateOnPress: Int = CardUnify2.ANIMATE_OVERLAY,
) : Visitable<CommonCarouselProductCardTypeFactory>, HomeComponentCarouselDiffUtil {

    companion object {
        const val ID = "VIEW_ALL_CARD"
    }
    override fun getId(): String = ID

    override fun equalsWith(visitable: Any?): Boolean {
        return visitable is CarouselViewAllCardDataModel
    }

    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}

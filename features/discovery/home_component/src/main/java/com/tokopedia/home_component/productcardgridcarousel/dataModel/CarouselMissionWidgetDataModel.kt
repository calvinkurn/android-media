package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by dhaba
 */
class CarouselMissionWidgetDataModel(
    val id: Long = 0L,
    val title: String = "",
    val subTitle: String = "",
    val appLink: String = "",
    val url: String = "",
    val imageURL: String = "",
    val pageName: String = "",
    val categoryID: String = "",
    val productID: String = "",
    val productName: String = "",
    val recommendationType: String = "",
    val buType: String = "",
    val isTopads: Boolean = false,
    val isCarousel: Boolean = false,
    val shopId: String = "",
    val subtitleHeight: Int = 0,
    val missionWidgetComponentListener: MissionWidgetComponentListener,
    val channel: ChannelModel,
    val verticalPosition: Int = 0
) : Visitable<CommonCarouselProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}

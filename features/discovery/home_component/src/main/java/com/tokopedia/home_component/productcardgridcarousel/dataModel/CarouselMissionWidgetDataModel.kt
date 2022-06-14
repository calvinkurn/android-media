package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
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
    val subtitleHeight: Int = 0,
    val missionWidgetComponentListener: MissionWidgetComponentListener
) : Visitable<CommonCarouselProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
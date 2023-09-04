package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselDiffUtil
import com.tokopedia.home_component.widget.mission.MissionWidgetTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by dhaba
 */
data class CarouselMissionWidgetDataModel(
    val data: MissionWidgetDataModel,
    val channelId: String,
    val channelName: String,
    val headerName: String,
    val subtitleHeight: Int,
    val verticalPosition: Int,
    val cardPosition: Int,
    val animateOnPress: Int,
) : Visitable<MissionWidgetTypeFactory>, HomeComponentCarouselDiffUtil, ImpressHolder() {

    override fun getId(): String {
        return channelId
    }

    override fun equalsWith(visitable: Any?): Boolean {
        return if(visitable is CarouselMissionWidgetDataModel)
            this == visitable
        else false
    }

    override fun type(typeFactory: MissionWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}

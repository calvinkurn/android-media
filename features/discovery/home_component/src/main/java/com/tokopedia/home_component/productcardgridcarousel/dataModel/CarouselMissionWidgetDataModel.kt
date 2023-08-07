package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
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
    val missionWidgetComponentListener: MissionWidgetComponentListener,
) : Visitable<CommonCarouselProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}

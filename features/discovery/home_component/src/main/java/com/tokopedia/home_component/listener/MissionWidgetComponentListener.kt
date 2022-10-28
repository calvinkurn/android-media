package com.tokopedia.home_component.listener

import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel

/**
 * Created by dhaba
 */
interface MissionWidgetComponentListener {
    fun refreshMissionWidget(missionWidgetListDataModel: MissionWidgetListDataModel)
    fun onMissionClicked(element: CarouselMissionWidgetDataModel, horizontalPosition: Int)
    fun onMissionImpressed(element: CarouselMissionWidgetDataModel, horizontalPosition: Int)
}
package com.tokopedia.home_component.viewholders.mission.v3

import com.tokopedia.home_component.visitable.MissionWidgetDataModel

interface Mission4SquareWidgetListener {
    fun onMissionClicked(model: MissionWidgetDataModel, position: Int)
    fun onMissionImpressed(model: MissionWidgetDataModel, position: Int)
}

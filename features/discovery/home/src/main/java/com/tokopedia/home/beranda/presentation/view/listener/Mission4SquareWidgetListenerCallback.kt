package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.viewholders.mission.v3.Mission4SquareWidgetListener
import com.tokopedia.home_component.visitable.MissionWidgetDataModel

class Mission4SquareWidgetListenerCallback(
    val homeCategoryListener: HomeCategoryListener
) : Mission4SquareWidgetListener {

    override fun onMissionClicked(model: MissionWidgetDataModel, position: Int) {
        homeCategoryListener.onDynamicChannelClicked(model.appLink)
    }

    override fun onMissionImpressed(model: MissionWidgetDataModel, position: Int) {

    }
}

package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.viewholders.mission.v3.Mission4SquareWidgetListener
import com.tokopedia.home_component.visitable.Mission4SquareUiModel

class Mission4SquareWidgetListenerCallback(
    val homeCategoryListener: HomeCategoryListener
) : Mission4SquareWidgetListener {

    override fun onMissionClicked(model: Mission4SquareUiModel, position: Int) {
        homeCategoryListener.onDynamicChannelClicked(model.data.appLink)
    }

    override fun onMissionImpressed(model: Mission4SquareUiModel, position: Int) {

    }
}

package com.tokopedia.home.beranda.presentation.view.listener.shorten.item

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.MissionWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel

class MissionTwoSquareWidgetCallback(val listener: HomeCategoryListener) : MissionWidgetListener {

    override fun missionClicked(data: ItemMissionWidgetUiModel, position: Int) {
        listener.onDynamicChannelClicked(data.appLink)
    }

    override fun missionImpressed(data: ItemMissionWidgetUiModel, position: Int) {

    }
}

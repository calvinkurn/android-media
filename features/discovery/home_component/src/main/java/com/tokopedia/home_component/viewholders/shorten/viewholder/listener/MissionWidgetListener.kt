package com.tokopedia.home_component.viewholders.shorten.viewholder.listener

import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel

interface MissionWidgetListener {
    // container
    fun missionChannelHeaderClicked(appLink: String)
    // fun missionContainerImpressed(data: MissionWidgetUiModel)

    // items
    fun missionImpressed(data: ItemMissionWidgetUiModel, position: Int)
    fun missionClicked(data: ItemMissionWidgetUiModel, position: Int)
}

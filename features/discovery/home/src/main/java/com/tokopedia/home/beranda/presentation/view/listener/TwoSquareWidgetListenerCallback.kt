package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.viewholders.shorten.ContainerMultiTwoSquareListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.DealsWidgetListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.MissionWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemDealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel

class TwoSquareWidgetListenerCallback(
    val homeCategoryListener: HomeCategoryListener
) : ContainerMultiTwoSquareListener(
    DealsTwoSquareWidgetCallback(homeCategoryListener),
    MissionTwoSquareWidgetCallback(homeCategoryListener)
)

class DealsTwoSquareWidgetCallback(val listener: HomeCategoryListener) : DealsWidgetListener {

    override fun dealsClicked(data: ItemDealsWidgetUiModel, position: Int) {
        listener.onDynamicChannelClicked(data.appLink)
    }

    override fun dealsImpressed(data: ItemDealsWidgetUiModel, position: Int) {

    }
}

class MissionTwoSquareWidgetCallback(val listener: HomeCategoryListener) : MissionWidgetListener {

    override fun missionClicked(data: ItemMissionWidgetUiModel, position: Int) {
        listener.onDynamicChannelClicked(data.appLink)
    }

    override fun missionImpressed(data: ItemMissionWidgetUiModel, position: Int) {

    }
}

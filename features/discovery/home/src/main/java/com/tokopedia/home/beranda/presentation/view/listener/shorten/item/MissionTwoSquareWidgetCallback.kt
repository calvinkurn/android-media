package com.tokopedia.home.beranda.presentation.view.listener.shorten.item

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.home.AppLogHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.analytics.TwoMissionSquareTrackingMapper.asCardModel
import com.tokopedia.home_component.analytics.TwoMissionSquareTrackingMapper.asProductModel
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.MissionWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel

class MissionTwoSquareWidgetCallback(val listener: HomeCategoryListener) : MissionWidgetListener {

    override fun missionClicked(data: ItemMissionWidgetUiModel, position: Int) {
        listener.onDynamicChannelClicked(data.appLink)

        if (data.tracker.isProduct()) { // check if is product
            AppLogHomeChannel.sendProductClick(data.asProductModel())
        } else {
            AppLogHomeChannel.sendCardClick(data.asCardModel())
        }

        AppLogAnalytics.putPageData(
            key = AppLogParam.ENTER_METHOD,
            value = AppLogHomeChannel.missionEnterMethod(
                data.tracker.recomPageName,
                position
            )
        )
    }

    override fun missionImpressed(data: ItemMissionWidgetUiModel, position: Int) {
        if (data.tracker.isProduct()) { // check if is product
            AppLogHomeChannel.sendProductShow(data.asProductModel())
        } else {
            AppLogHomeChannel.sendCardShow(data.asCardModel())
        }
    }
}

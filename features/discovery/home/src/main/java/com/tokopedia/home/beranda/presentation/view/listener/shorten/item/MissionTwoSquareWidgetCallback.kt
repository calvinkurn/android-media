package com.tokopedia.home.beranda.presentation.view.listener.shorten.item

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.home.AppLogHomeChannel
import com.tokopedia.home.analytics.v2.Mission4SquareWidgetTracker
import com.tokopedia.home.analytics.v2.TwoSquareWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.analytics.TwoMissionSquareTrackingMapper.asCardModel
import com.tokopedia.home_component.analytics.TwoMissionSquareTrackingMapper.asProductModel
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.MissionWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel
import com.tokopedia.track.TrackApp
import java.util.HashMap

class MissionTwoSquareWidgetCallback(val listener: HomeCategoryListener) : MissionWidgetListener {

    override fun missionChannelHeaderClicked(appLink: String) {
        listener.onDynamicChannelClicked(appLink)
    }

    override fun missionClicked(data: ItemMissionWidgetUiModel, position: Int) {
        listener.onDynamicChannelClicked(data.appLink)

        if (data.tracker.isProduct()) { // check if is product
            Mission4SquareWidgetTracker.sendMissionWidgetClicked(data, position, listener.userId)
            AppLogHomeChannel.sendProductClick(data.asProductModel())
        } else {
            Mission4SquareWidgetTracker.sendMissionWidgetClickedToPdp(data, position, listener.userId)
            AppLogHomeChannel.sendCardClick(data.asCardModel())
        }

        AppLogAnalytics.putPageData(
            key = AppLogParam.ENTER_METHOD,
            value = AppLogHomeChannel.getEnterMethod(
                data.tracker.layoutTrackerType,
                data.tracker.recomPageName,
                position
            )
        )
    }

    override fun missionImpressed(data: ItemMissionWidgetUiModel, position: Int) {
        if (data.tracker.isProduct()) { // check if is product
            listener.getTrackingQueueObj()?.putEETracking(
                Mission4SquareWidgetTracker.getMissionWidgetProductView(data, position, listener.userId) as HashMap<String, Any>
            )

            AppLogHomeChannel.sendProductShow(data.asProductModel())
        } else {
            listener.getTrackingQueueObj()?.putEETracking(
                Mission4SquareWidgetTracker.getMissionWidgetView(data, position, listener.userId) as HashMap<String, Any>
            )

            AppLogHomeChannel.sendCardShow(data.asCardModel())
        }
    }
}

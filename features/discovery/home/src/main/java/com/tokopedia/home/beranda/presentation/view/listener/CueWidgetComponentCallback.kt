package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.CueCategoryTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.CueWidgetCategoryListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.TrackApp

/**
 * Created by dhaba
 */
class CueWidgetComponentCallback(val homeCategoryListener: HomeCategoryListener) :
    CueWidgetCategoryListener {
    override fun onCueClick(
        channelGrid: ChannelGrid,
        channelModel: ChannelModel,
        positionVerticalWidget: Int,
        positionHorizontal: Int,
        widgetGridType: String
    ) {
        val tracking = CueCategoryTracking.getCueWidgetClick(
            channelGrid,
            homeCategoryListener.userId,
            positionHorizontal,
            channelModel,
            positionVerticalWidget,
            widgetGridType
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(channelGrid.applink)
    }

    override fun onCueImpressed(
        channelGrid: ChannelGrid,
        channelModel: ChannelModel,
        positionVerticalWidget: Int,
        positionHorizontal: Int,
        widgetGridType: String
    ) {
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
            CueCategoryTracking.getCueCategoryView(
                channelGrid,
                homeCategoryListener.userId,
                positionHorizontal,
                channelModel,
                positionVerticalWidget,
                widgetGridType
            ) as HashMap<String, Any>
        )
    }
}
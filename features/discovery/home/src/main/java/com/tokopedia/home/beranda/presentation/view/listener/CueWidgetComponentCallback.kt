package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.CueCategoryTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.CueWidgetCategoryListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by dhaba
 */
class CueWidgetComponentCallback (val homeCategoryListener: HomeCategoryListener) : CueWidgetCategoryListener {
    override fun onCueClick(channelGrid: ChannelGrid) {
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
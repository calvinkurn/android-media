package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.LegoBannerTracking
import com.tokopedia.home.analytics.v2.VpsWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.VpsWidgetListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by frenzel
 */
class VpsWidgetComponentCallback(val homeCategoryListener: HomeCategoryListener): VpsWidgetListener {
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
    }

    override fun onItemImpressed(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        parentPosition: Int
    ) {

    }

    override fun onItemClicked(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        parentPosition: Int
    ) {
        VpsWidgetTracking.sendVpsItemClick(channelModel, channelGrid, position, homeCategoryListener.userId)
        homeCategoryListener.onDynamicChannelClicked(channelGrid.applink)
    }

    override fun onChannelImpressed(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToTrackingQueue(
            VpsWidgetTracking.getVpsImpression(channelModel, parentPosition, homeCategoryListener.userId)
        )
    }
}
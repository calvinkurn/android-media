package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.LegoBannerTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.VpsWidgetListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by yoasfs on 28/07/20
 */
class VpsWidgetComponentCallback(val context: Context?, val homeCategoryListener: HomeCategoryListener): VpsWidgetListener {
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
        LegoBannerTracking.sendLegoBannerFourClickViewAll(channelModel, channelModel.id, homeCategoryListener.userId)
        RouteManager.route(context,
                if (channelModel.channelHeader.applink.isNotEmpty())
                    channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onItemImpressed(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        parentPosition: Int
    ) {
        //tracker
    }

    override fun onItemClicked(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        parentPosition: Int
    ) {
        //tracker
        RouteManager.route(context, channelGrid.applink)
    }

    override fun onChannelImpressed(channelModel: ChannelModel, parentPosition: Int) {
        //tracker
    }
}
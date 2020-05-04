package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.TrackApp

class DynamicLegoBannerComponentCallback(val context: Context): DynamicLegoBannerListener {
    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        HomePageTracking.eventClickSeeAllLegoBannerChannel(
                context,
                channelModel.channelHeader.applink,
                channelModel.id)
        RouteManager.route(context,
                if (channelModel.channelHeader.applink.isNotEmpty())
                    channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(HomePageTrackingV2.LegoBanner.getLegoBannerFourImageSeeAllClick(channelModel))
        RouteManager.route(context,
                if (channelModel.channelHeader.applink.isNotEmpty())
                    channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        HomePageTracking.eventClickSeeAllThreeLegoBannerChannel(
                context,
                channelModel.channelHeader.applink,
                channelModel.id)
        RouteManager.route(context,
                if (channelModel.channelHeader.applink.isNotEmpty())
                    channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                HomePageTracking.getEnhanceClickLegoBannerHomePage(
                        channelGrid,
                        channelModel,
                        position + 1)
        )
        RouteManager.route(context,
                if (channelGrid.applink.isNotEmpty())
                    channelGrid.applink else channelGrid.url)
    }

    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                HomePageTrackingV2.LegoBanner.getLegoBannerFourImageClick(
                        channelModel,
                        channelGrid,
                        position + 1)
        )
        RouteManager.route(context,
                if (channelGrid.applink.isNotEmpty())
                    channelGrid.applink else channelGrid.url)
    }

    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                HomePageTracking.getEnhanceClickThreeLegoBannerHomePage(
                        channelModel,
                        channelGrid,
                        position + 1)
        )
        RouteManager.route(context,
                if (channelGrid.applink.isNotEmpty())
                    channelGrid.applink else channelGrid.url)
    }

    override fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {

    }
}
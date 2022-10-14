package com.tokopedia.dilayanitokopedia.home.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel


class DtHomeLeftCarouselCallback(
    private val context: Context,
//    private val analytics: HomeAnalytics
) : MixLeftComponentListener {

    override fun onMixLeftImpressed(channel: ChannelModel, parentPos: Int) {
//        trackImpression(channel)
    }

    override fun onImageBannerImpressed(channelModel: ChannelModel, position: Int) {
    }

    override fun onImageBannerClicked(channelModel: ChannelModel, position: Int, applink: String) {
        openAppLink(applink)
//        trackClickBanner(channelModel)
    }

    override fun onSeeAllBannerClicked(channel: ChannelModel, applink: String) {
        openAppLink(applink)
//        trackClickViewAllEvent(channel)
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        adapterPosition: Int,
        position: Int
    ) {
//        trackProductImpression(position, channel, channelGrid)
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        adapterPosition: Int,
        position: Int,
        applink: String
    ) {
        openAppLink(applink)
//        trackClickProduct(position, channel, channelGrid)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        openAppLink(applink)
//        trackClickViewAllEvent(channel)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        openAppLink(applink)
//        trackClickBanner(channel)
    }
//    tokopedia://product/6099214630?extParam=src%3Dcampaign%26whid%3D7025696
    private fun openAppLink(appLink: String) {
        if (appLink.isNotEmpty()) {
            RouteManager.route(context, appLink)
        }
    }

//    private fun trackImpression(channel: ChannelModel) {
//        analytics.trackImpressionLeftCarousel(channel.id, channel.channelHeader.name)
//    }
//
//    private fun trackProductImpression(position: Int, channel: ChannelModel, grid: ChannelGrid) {
//        analytics.trackImpressionProductLeftCarousel(position, channel, grid)
//    }
//
//    private fun trackClickProduct(position: Int, channel: ChannelModel, grid: ChannelGrid) {
//        analytics.trackClickProductLeftCarousel(position, channel, grid)
//    }
//
//    private fun trackClickBanner(channel: ChannelModel) {
//        analytics.trackClickBannerLeftCarousel(channel.id, channel.channelHeader.name)
//    }
//
//    private fun trackClickViewAllEvent(channel: ChannelModel) {
//        analytics.trackClickViewAllLeftCarousel(channel.id, channel.channelHeader.name)
//    }
}
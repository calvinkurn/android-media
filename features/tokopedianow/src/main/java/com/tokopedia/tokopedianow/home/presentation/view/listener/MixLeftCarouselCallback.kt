package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics

class MixLeftCarouselCallback(
    view: TokoNowView,
    private val analytics: HomeAnalytics
): MixLeftComponentListener {

    private val context by lazy { view.getFragmentPage().context }

    override fun onMixLeftImpressed(channel: ChannelModel, parentPos: Int) {
        trackImpression(channel)
    }

    override fun onImageBannerImpressed(channelModel: ChannelModel, position: Int) {
    }

    override fun onImageBannerClicked(channelModel: ChannelModel, position: Int, applink: String) {
        openAppLink(applink)
        trackClickBanner(channelModel)
    }

    override fun onSeeAllBannerClicked(channel: ChannelModel, applink: String) {
        openAppLink(applink)
        trackClickViewAllEvent(channel)
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        adapterPosition: Int,
        position: Int
    ) {
        trackProductImpression(position, channel, channelGrid)
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        adapterPosition: Int,
        position: Int,
        applink: String
    ) {
        openAppLink(applink)
        trackClickProduct(position, channel, channelGrid)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        openAppLink(applink)
        trackClickViewAllEvent(channel)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        openAppLink(applink)
        trackClickBanner(channel)
    }

    private fun openAppLink(appLink: String) {
        if(appLink.isNotEmpty()) {
            RouteManager.route(context, appLink)
        }
    }

    private fun trackImpression(channel: ChannelModel) {
        analytics.trackImpressionLeftCarousel(channel.id, channel.channelHeader.name)
    }

    private fun trackProductImpression(position: Int, channel: ChannelModel, grid: ChannelGrid) {
        analytics.trackImpressionProductLeftCarousel(position, channel, grid)
    }

    private fun trackClickProduct(position: Int, channel: ChannelModel, grid: ChannelGrid) {
        analytics.trackClickProductLeftCarousel(position, channel, grid)
    }

    private fun trackClickBanner(channel: ChannelModel) {
        analytics.trackClickBannerLeftCarousel(channel.id, channel.channelHeader.name)
    }

    private fun trackClickViewAllEvent(channel: ChannelModel) {
        analytics.trackClickViewAllLeftCarousel(channel.id, channel.channelHeader.name)
    }
}
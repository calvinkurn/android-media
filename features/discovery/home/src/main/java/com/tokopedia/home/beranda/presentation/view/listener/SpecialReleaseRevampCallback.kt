package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.SpecialReleaseRevampTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.widget.special_release.SpecialReleaseRevampListener

class SpecialReleaseRevampCallback(
    private val homeCategoryListener: HomeCategoryListener
): SpecialReleaseRevampListener {
    override fun onShopClicked(channel: ChannelModel, channelGrid: ChannelGrid, applink: String) {
        SpecialReleaseRevampTracking.sendClickShop(channel, channelGrid, homeCategoryListener.userId)
        homeCategoryListener.onDynamicChannelClicked(channelGrid.applink)
    }

    override fun onShopImpressed(channel: ChannelModel, channelGrid: ChannelGrid) {
        val tracker = SpecialReleaseRevampTracking.getImpressionShop(channel, channelGrid, homeCategoryListener.userId)
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(tracker)
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        val tracker = SpecialReleaseRevampTracking.getImpressionProduct(channel, channelGrid, homeCategoryListener.userId)
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(tracker)
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
        SpecialReleaseRevampTracking.sendClickProduct(channel, channelGrid, homeCategoryListener.userId)
        homeCategoryListener.onDynamicChannelClicked(channelGrid.applink)
    }

    override fun onSeeAllClick(channel: ChannelModel, link: String) {
        SpecialReleaseRevampTracking.sendClickViewAll(channel)
        homeCategoryListener.onDynamicChannelClicked(link)
    }
}

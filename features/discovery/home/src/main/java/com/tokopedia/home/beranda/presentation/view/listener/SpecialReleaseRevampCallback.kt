package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.SpecialReleaseRevampTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.widget.special_release.SpecialReleaseRevampListener

class SpecialReleaseRevampCallback(
    private val homeCategoryListener: HomeCategoryListener
): SpecialReleaseRevampListener {
    override fun onShopClicked(trackingAttributionModel: TrackingAttributionModel, channelGrid: ChannelGrid, applink: String) {
        if(applink.isNotEmpty()) {
            SpecialReleaseRevampTracking.sendClickShop(trackingAttributionModel, channelGrid, homeCategoryListener.userId)
            homeCategoryListener.onDynamicChannelClicked(applink)
        }
    }

    override fun onShopImpressed(trackingAttributionModel: TrackingAttributionModel, channelGrid: ChannelGrid) {
        val tracker = SpecialReleaseRevampTracking.getImpressionShop(trackingAttributionModel, channelGrid, homeCategoryListener.userId)
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(tracker)
    }

    override fun onProductCardImpressed(
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        val tracker = SpecialReleaseRevampTracking.getImpressionProduct(trackingAttributionModel, channelGrid, homeCategoryListener.userId)
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(tracker)
    }

    override fun onProductCardClicked(
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
        SpecialReleaseRevampTracking.sendClickProduct(trackingAttributionModel, channelGrid, homeCategoryListener.userId)
        homeCategoryListener.onDynamicChannelClicked(applink)
    }

    override fun onSeeAllClick(trackingAttributionModel: TrackingAttributionModel, link: String) {
        SpecialReleaseRevampTracking.sendClickViewAll(trackingAttributionModel)
        homeCategoryListener.onDynamicChannelClicked(link)
    }
}

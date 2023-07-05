package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.FlashSaleTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.FlashSaleWidgetListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import java.util.HashMap

class FlashSaleWidgetCallback(
    val homeCategoryListener: HomeCategoryListener
) : FlashSaleWidgetListener {
    override fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid) {
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
            FlashSaleTracking.getImpressionEvent(
                channelGrid,
                channel,
                homeCategoryListener.userId
            ) as? HashMap<String, Any>
        )
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        applink: String
    ) {
        FlashSaleTracking.sendClickEvent(channelGrid, channel, homeCategoryListener.userId)
        homeCategoryListener.onDynamicChannelClicked(applink)
    }

    override fun onSeeAllClicked(channel: ChannelModel, applink: String) {
        FlashSaleTracking.sendHeaderCtaClickEvent(channel)
        homeCategoryListener.onDynamicChannelClicked(applink)
    }

    override fun onViewAllCardClicked(channel: ChannelModel, applink: String) {
        FlashSaleTracking.sendViewAllCardClickEvent(channel)
        homeCategoryListener.onDynamicChannelClicked(applink)
    }
}

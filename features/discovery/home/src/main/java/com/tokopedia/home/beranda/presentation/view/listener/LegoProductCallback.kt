package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.LegoProductTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.LegoProductListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class LegoProductCallback(val homeCategoryListener: HomeCategoryListener) : LegoProductListener {
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
        LegoProductTracking.sendLego4ProductSeeAllClick(channelModel)
        homeCategoryListener.onDynamicChannelClicked(applink = channelModel.channelHeader.applink)
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
        LegoProductTracking.sendLego4ProductItemClick(channel, channelGrid, position, applink)
        homeCategoryListener.onDynamicChannelClicked(applink = applink)
    }

    override fun onChannelImpressed(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToTrackingQueue(LegoProductTracking.getLego4ProductImpression(channelModel, parentPosition, homeCategoryListener.userId))
    }
}

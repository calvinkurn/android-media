package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by frenzel
 */
interface FlashSaleWidgetListener {
    fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid)
    fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, applink: String)
    fun onSeeAllClicked(channel: ChannelModel, applink: String)
    fun onViewAllCardClicked(channel: ChannelModel, applink: String)
}

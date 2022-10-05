package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by frenzel
 */
interface LegoProductCardListener {
    fun onSeeAllClicked(channelModel: ChannelModel, position: Int)
    fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, position: Int)
    fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String)
    fun onChannelImpressed(channelModel: ChannelModel, parentPosition: Int)
}

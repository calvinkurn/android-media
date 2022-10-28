package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by frenzel
 */

interface VpsWidgetListener {

    fun onSeeAllClicked(channelModel: ChannelModel, position: Int)

    fun onItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)

    fun onItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)

    fun onChannelImpressed(channelModel: ChannelModel, parentPosition: Int)
}
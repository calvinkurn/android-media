package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by Devara on 09/02/21
 */

interface Lego6AutoBannerListener {

    fun onSeeAllClicked(channelModel: ChannelModel, position: Int)

    fun onLegoItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)

    fun onTallImageClicked(channelModel: ChannelModel, parentPosition: Int)

    fun onLegoItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)

    fun onChannelLegoImpressed(channelModel: ChannelModel, parentPosition: Int)
}
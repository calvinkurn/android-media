package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by yoasfs on 27/07/20
 */

interface FeaturedBrandListener {

    fun onSeeAllClicked(channelModel: ChannelModel, position: Int)

    fun onLegoItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)

    fun onLegoItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)

    fun onChannelLegoImpressed(channelModel: ChannelModel, parentPosition: Int)
}
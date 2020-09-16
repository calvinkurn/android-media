package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by Lukas on 27/07/20
 */

interface FeaturedShopListener {

    fun onSeeAllClicked(channelModel: ChannelModel, position: Int)

    fun onSeeAllBannerClicked(channelModel: ChannelModel, applink: String, position: Int)

    fun onFeaturedShopBannerBackgroundClicked(channel: ChannelModel)

    fun onFeaturedShopItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)

    fun onFeaturedShopItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)
}
package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.home_component.listener.FeaturedShopListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler

/**
 * Created by yfsx on 05/08/21.
 */
class OSFeaturedShopDCCallback(private val dcEventHandler: DynamicChannelEventHandler) : FeaturedShopListener {
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
    }

    override fun onSeeAllBannerClicked(channelModel: ChannelModel, applink: String, position: Int) {
        dcEventHandler.onSeeAllFeaturedShopDCClicked(channelModel, position, applink)
    }

    override fun onFeaturedShopBannerBackgroundClicked(channel: ChannelModel) {
    }

    override fun onFeaturedShopItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.onFeaturedShopDCImpressed(channelGrid, position)
    }

    override fun onFeaturedShopItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.onFeaturedShopDCClicked(channelGrid, position, channelGrid.applink)
    }
}
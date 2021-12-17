package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.FeaturedShopListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler

/**
 * Created by yfsx on 05/08/21.
 */
class OSFeaturedShopDCCallback(private val dcEventHandler: DynamicChannelEventHandler) : FeaturedShopListener {
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
        dcEventHandler.onSeeAllFeaturedShopDCClicked(channelModel, position, channelModel.channelHeader.applink)
    }

    override fun onSeeAllBannerClicked(channelModel: ChannelModel, applink: String, position: Int) {
        dcEventHandler.onSeeAllFeaturedShopDCClicked(channelModel, position, applink)
    }

    override fun onFeaturedShopBannerBackgroundClicked(channel: ChannelModel) {
        var applink = channel.channelBanner.applink
        dcEventHandler.goToApplink(applink)
    }

    override fun onFeaturedShopItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.onFeaturedShopDCImpressed(channelModel, channelGrid, position, parentPosition)
    }

    override fun onFeaturedShopItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.onFeaturedShopDCClicked(channelModel, channelGrid, position, parentPosition)
    }
}
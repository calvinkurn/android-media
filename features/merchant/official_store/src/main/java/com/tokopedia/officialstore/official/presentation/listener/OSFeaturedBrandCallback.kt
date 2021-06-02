package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.home_component.listener.FeaturedBrandListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler

/**
 * Created by yfsx on 5/31/21.
 */
class OSFeaturedBrandCallback (private val dcEventHandler: DynamicChannelEventHandler, private val tracking: OfficialStoreTracking?): FeaturedBrandListener {
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
        tracking?.eventClickAllFeaturedBrand(channelModel.name)
    }

    override fun onLegoItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
    }

    override fun onLegoItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
    }

    override fun onChannelLegoImpressed(channelModel: ChannelModel, parentPosition: Int) {

    }
}
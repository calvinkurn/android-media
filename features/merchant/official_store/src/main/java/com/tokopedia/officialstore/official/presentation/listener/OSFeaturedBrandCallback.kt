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
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int, applink: String) {
        tracking?.eventClickAllFeaturedBrandOS(dcEventHandler.getOSCategory()?.title ?: "")
        dcEventHandler.goToApplink(applink)
    }

    override fun onLegoItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        val defaultId = "0"
        val bannerId = if(channelModel.channelBanner.id.isBlank()) defaultId else channelModel.channelBanner.id
        val shopId = if(channelGrid.shopId.isBlank()) defaultId else channelGrid.shopId
        tracking?.eventImpressionFeatureBrandOS(
            categoryName = dcEventHandler.getOSCategory()?.title ?: "",
            shopPosition = (position + 1),
            shopId = shopId,
            creativeName = channelGrid.attribution,
            userId = dcEventHandler.getUserId(),
            headerName = channelModel.channelHeader.name,
            bannerId =  bannerId
        )
    }

    override fun onLegoItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int, applink: String) {
        val defaultId = "0"
        val bannerId = if(channelModel.channelBanner.id.isBlank()) defaultId else channelModel.channelBanner.id
        val shopId = if(channelGrid.shopId.isBlank()) defaultId else channelGrid.shopId
        tracking?.eventClickFeaturedBrandOS(
            categoryName = dcEventHandler.getOSCategory()?.title ?: "",
            shopPosition = (position + 1),
            shopId = shopId,
            creativeName = channelGrid.attribution,
            userId = dcEventHandler.getUserId(),
            headerName = channelModel.channelHeader.name,
            bannerId =  bannerId
        )

        dcEventHandler.goToApplink(applink)
    }

    override fun onChannelLegoImpressed(channelModel: ChannelModel, parentPosition: Int) {

    }
}
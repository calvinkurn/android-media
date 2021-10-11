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
        tracking?.eventClickAllFeaturedBrand(dcEventHandler.getOSCategory()?.title ?: "")
    }

    override fun onLegoItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        tracking?.eventImpressionFeatureBrand(
                categoryName = dcEventHandler.getOSCategory()?.title ?: "",
                shopPosition = (position + 1),
                shopName = channelGrid.shop.shopName,
                url = channelGrid.shop.shopProfileUrl,
                additionalInformation = "",
                featuredBrandId = "",
                isLogin = dcEventHandler.isLogin(),
                shopId = channelGrid.shopId,
                isFromDC = true,
                attribute = channelGrid.attribution
        )
    }

    override fun onLegoItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int, applink: String) {
        tracking?.eventClickFeaturedBrand(
                categoryName = dcEventHandler.getOSCategory()?.title ?: "",
                shopPosition = (position + 1),
                shopName = channelGrid.shop.shopName,
                url = channelGrid.shop.shopProfileUrl,
                additionalInformation = "",
                featuredBrandId = "",
                isLogin = dcEventHandler.isLogin(),
                shopId = channelGrid.shopId,
                campaignCode = channelGrid.campaignCode,
                isFromDC = true,
                attribute = channelGrid.attribution
        )
        dcEventHandler.goToApplink(applink)
    }

    override fun onChannelLegoImpressed(channelModel: ChannelModel, parentPosition: Int) {

    }
}
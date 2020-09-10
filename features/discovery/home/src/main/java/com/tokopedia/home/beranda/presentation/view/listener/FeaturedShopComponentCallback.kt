package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.FeaturedShopTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.FeaturedShopListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by Lukas on 09/09/20.
 */
class FeaturedShopComponentCallback(private val homeCategoryListener: HomeCategoryListener) : FeaturedShopListener{
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
        FeaturedShopTracking.sendFeaturedShopViewAllClick(channelModel, channelModel.id, homeCategoryListener.userId)
    }

    override fun onSeeAllBannerClicked(channelModel: ChannelModel, position: Int) {
        FeaturedShopTracking.sendFeaturedShopViewAllCardClick(channelModel, channelModel.id, homeCategoryListener.userId)
    }

    override fun onFeaturedShopBannerBackgroundClicked(channelModel: ChannelModel) {
        FeaturedShopTracking.sendFeaturedShopBackgroundClick(channelModel, channelModel.id, homeCategoryListener.userId)
    }

    override fun onFeaturedShopItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        FeaturedShopTracking.getFeaturedShopItemImpression(channelModel, channelGrid, homeCategoryListener.userId, parentPosition, position)
    }

    override fun onFeaturedShopItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        FeaturedShopTracking.sendFeaturedShopItemClick(channelModel, channelGrid, homeCategoryListener.userId, parentPosition, position)
    }

}
package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.FeaturedShopTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.FeaturedShopListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import java.util.HashMap

/**
 * Created by Lukas on 09/09/20.
 */
class FeaturedShopComponentCallback(val context: Context?, private val homeCategoryListener: HomeCategoryListener) : FeaturedShopListener{
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
        FeaturedShopTracking.sendFeaturedShopViewAllClick(channelModel, channelModel.id, homeCategoryListener.userId)
        RouteManager.route(context, channelModel.channelHeader.applink)
    }

    override fun onSeeAllBannerClicked(channelModel: ChannelModel, applink: String, position: Int) {
        FeaturedShopTracking.sendFeaturedShopViewAllCardClick(channelModel, channelModel.id, homeCategoryListener.userId)
        RouteManager.route(context, applink)
    }

    override fun onFeaturedShopBannerBackgroundClicked(channelModel: ChannelModel) {
        FeaturedShopTracking.sendFeaturedShopBackgroundClick(channelModel, channelModel.id, homeCategoryListener.userId)
        RouteManager.route(context, channelModel.channelBanner.applink)
    }

    override fun onFeaturedShopItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        homeCategoryListener.putEEToTrackingQueue(FeaturedShopTracking.getFeaturedShopItemImpression(channelModel, channelGrid, homeCategoryListener.userId, parentPosition, position) as HashMap<String, Any>)
    }

    override fun onFeaturedShopItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        FeaturedShopTracking.sendFeaturedShopItemClick(channelModel, channelGrid, homeCategoryListener.userId, parentPosition, position)
        RouteManager.route(context, channelGrid.applink)
    }

}
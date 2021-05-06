package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.RecommendationListTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.RecommendationListCarouselListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.TrackApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.HashMap

@ExperimentalCoroutinesApi
class RecommendationListCarouselComponentCallback(val homeCategoryListener: HomeCategoryListener): RecommendationListCarouselListener {
    override fun onBuyAgainCloseChannelClick(channelModel: ChannelModel, position: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(RecommendationListTracking.getCloseClickOnDynamicListCarouselHomeComponent(channelModel, homeCategoryListener.getUserIdFromViewModel()))
        homeCategoryListener.recommendationListOnCloseBuyAgain(channelModel.id, position = position)
    }

    override fun onRecommendationSeeMoreClick(channelModel: ChannelModel, applink: String) {
        RecommendationListTracking.sendRecommendationListSeeAllClick(channelModel.id, channelModel.channelHeader.name, homeCategoryListener.getUserIdFromViewModel())
        homeCategoryListener.onDynamicChannelClicked(applink)
    }

    override fun onRecommendationSeeMoreCardClick(channelModel: ChannelModel, applink: String) {
        RecommendationListTracking.sendRecommendationListSeeAllCardClick(channelModel.id, channelModel.channelHeader.name, homeCategoryListener.getUserIdFromViewModel())
        homeCategoryListener.onDynamicChannelClicked(applink)
    }

    override fun onRecommendationProductClick(channelModel: ChannelModel,
                                              channelGrid: ChannelGrid,
                                              position: Int,
                                              applink: String,
                                              parentPosition: Int) {
        RecommendationListTracking.sendRecommendationListHomeComponentClick(channelModel, channelGrid, position, homeCategoryListener.getUserIdFromViewModel(), parentPosition)
        homeCategoryListener.onSectionItemClicked(applink)
    }

    override fun onBuyAgainOneClickCheckOutClick(channelGrid: ChannelGrid, channelModel: ChannelModel, position: Int) {
        homeCategoryListener.getOneClickCheckoutHomeComponent(channelModel, channelGrid, position)
    }

    override fun onRecommendationCarouselChannelImpression(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToIris(RecommendationListTracking.getRecommendationListImpression(channelModel,true, homeCategoryListener.userId, parentPosition ) as HashMap<String, Any>)

    }

    override fun onRecommendationCarouselGridImpression(channelModel: ChannelModel, channelGrid: ChannelGrid?, gridPosition: Int, parentPosition: Int, isSeeMoreView: Boolean) {
        homeCategoryListener.putEEToTrackingQueue(RecommendationListTracking.getRecommendationListProductImpression(channelModel, channelGrid!!,false, homeCategoryListener.userId, gridPosition, parentPosition ) as HashMap<String, Any>)
    }
}
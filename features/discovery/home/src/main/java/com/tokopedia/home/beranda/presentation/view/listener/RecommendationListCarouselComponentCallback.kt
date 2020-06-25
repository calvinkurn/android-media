package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home_component.listener.RecommendationListCarouselListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.TrackApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.HashMap

@ExperimentalCoroutinesApi
class RecommendationListCarouselComponentCallback(val homeViewModel: HomeViewModel,
                                                  val homeCategoryListener: HomeCategoryListener): RecommendationListCarouselListener {
    override fun onBuyAgainCloseChannelClick(channelModel: ChannelModel, position: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(HomePageTrackingV2.RecommendationList.getCloseClickOnDynamicListCarouselHomeComponent(channelModel, homeViewModel.getUserId()))
        homeViewModel.onCloseBuyAgain(channelModel.id, position = position)
    }

    override fun onRecommendationSeeMoreClick(channelModel: ChannelModel, applink: String) {
        HomePageTrackingV2.RecommendationList.sendRecommendationListSeeAllClick(channelModel.id, channelModel.channelHeader.name, homeViewModel.getUserId())
        homeCategoryListener.onDynamicChannelClicked(applink)
    }

    override fun onRecommendationProductClick(channelModel: ChannelModel,
                                              channelGrid: ChannelGrid,
                                              position: Int,
                                              applink: String) {
        HomePageTrackingV2.RecommendationList.sendRecommendationListHomeComponentClick(channelModel, channelGrid, position, homeViewModel.getUserId())
        homeCategoryListener.onSectionItemClicked(applink)
    }

    override fun onBuyAgainOneClickCheckOutClick(channelGrid: ChannelGrid, channelModel: ChannelModel, position: Int) {
        homeViewModel.getOneClickCheckoutHomeComponent(channelModel, channelGrid, position)
    }

    override fun onRecommendationCarouselChannelImpression(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToIris(HomePageTrackingV2.RecommendationList.getRecommendationListImpression(channelModel, true, homeCategoryListener.userId) as HashMap<String, Any>)
    }

    override fun onRecommendationCarouselGridImpression(channelModel: ChannelModel, channelGrid: ChannelGrid?, gridPosition: Int, parentPosition: Int, isSeeMoreView: Boolean) {

    }
}
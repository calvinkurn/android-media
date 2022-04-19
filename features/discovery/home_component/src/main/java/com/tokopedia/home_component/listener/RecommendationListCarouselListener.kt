package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

interface RecommendationListCarouselListener {
    fun onBuyAgainCloseChannelClick(channelModel: ChannelModel, position: Int)
    fun onRecommendationSeeMoreClick(channelModel: ChannelModel, applink: String)
    fun onRecommendationSeeMoreCardClick(channelModel: ChannelModel, applink: String)
    fun onRecommendationProductClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String, parentPosition: Int)
    fun onBuyAgainOneClickCheckOutClick(channelGrid: ChannelGrid, channelModel: ChannelModel, position: Int)
    fun onRecommendationCarouselChannelImpression(channelModel: ChannelModel, parentPosition: Int)
    fun onRecommendationCarouselGridImpression(channelModel: ChannelModel,
                                               channelGrid: ChannelGrid?,
                                               gridPosition: Int,
                                               parentPosition: Int,
                                               isSeeMoreView: Boolean)
}
package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by yfsx on 5/3/21.
 */
interface RecommendationCarouselWidgetListener {

    fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int)
    fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String)
    fun onRecomChannelImpressed(data: RecommendationCarouselData)
    fun onRecomProductCardImpressed(data: RecommendationCarouselData, recomItem: RecommendationItem, itemPosition: Int, adapterPosition: Int)
    fun onRecomProductCardClicked(data: RecommendationCarouselData, recomItem: RecommendationItem, applink: String, itemPosition: Int, adapterPosition: Int)
    fun onRecomBannerImpressed(data: RecommendationCarouselData, adapterPosition: Int)
    fun onRecomBannerClicked(data: RecommendationCarouselData, applink: String, adapterPosition: Int)
}
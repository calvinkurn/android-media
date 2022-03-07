package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by yfsx on 06/10/21.
 */
interface RecomCarouselWidgetBasicListener {
    fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int)
    fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String)
    fun onRecomChannelImpressed(data: RecommendationCarouselData)
    fun onRecomProductCardImpressed(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    )

    fun onRecomProductCardClicked(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        applink: String,
        itemPosition: Int,
        adapterPosition: Int
    )

    fun onRecomBannerImpressed(data: RecommendationCarouselData, adapterPosition: Int)
    fun onRecomBannerClicked(
        data: RecommendationCarouselData,
        applink: String,
        adapterPosition: Int
    )

    fun onChannelWidgetEmpty()
    fun onWidgetFail(pageName: String, e: Throwable)
    fun onShowError(pageName: String, e: Throwable)
}
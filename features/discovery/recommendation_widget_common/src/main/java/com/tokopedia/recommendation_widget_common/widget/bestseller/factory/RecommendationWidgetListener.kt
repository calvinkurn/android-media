package com.tokopedia.recommendation_widget_common.widget.bestseller.factory

import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel

/**
 * Created by Lukas on 05/11/20.
 */
interface RecommendationWidgetListener {
    fun onBestSellerClick(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int)
    fun onBestSellerImpress(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int)
    fun onBestSellerThreeDotsClick(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int)
    fun onBestSellerFilterClick(filter: RecommendationFilterChipsEntity.RecommendationFilterChip, bestSellerDataModel: BestSellerDataModel, widgetPosition: Int, chipsPosition: Int)
    fun onBestSellerSeeMoreTextClick(bestSellerDataModel: BestSellerDataModel, appLink: String, widgetPosition: Int)
    fun onBestSellerSeeAllCardClick(bestSellerDataModel: BestSellerDataModel, appLink: String, widgetPosition: Int)
}
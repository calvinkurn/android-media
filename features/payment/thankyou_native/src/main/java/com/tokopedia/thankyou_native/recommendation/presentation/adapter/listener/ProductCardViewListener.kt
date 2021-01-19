package com.tokopedia.thankyou_native.recommendation.presentation.adapter.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface ProductCardViewListener {
    fun onProductClick(item: RecommendationItem,
                       layoutType: String?, vararg position: Int)
    fun onProductImpression(item: RecommendationItem, position: Int)
    fun onRecommendationItemDisplayed(recommendationItem: RecommendationItem, position: Int)
}

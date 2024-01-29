package com.tokopedia.recommendation_widget_common.widget.foryou

import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationModel

interface ParentRecommendationListener {
    fun onRetryGetProductRecommendationData()
    fun onRetryGetNextProductRecommendationData()

    fun onProductImpression(model: HomeRecommendationModel, position: Int)
    fun onProductClick(model: HomeRecommendationModel, position: Int)
    fun onProductThreeDotsClick(model: HomeRecommendationModel, position: Int)
}

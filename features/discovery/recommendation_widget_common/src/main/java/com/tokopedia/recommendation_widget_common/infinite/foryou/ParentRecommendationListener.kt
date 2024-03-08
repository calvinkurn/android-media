package com.tokopedia.recommendation_widget_common.infinite.foryou

import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel

interface ParentRecommendationListener {
    fun onRetryGetProductRecommendationData()
    fun onRetryGetNextProductRecommendationData()

    fun onProductCardImpressed(model: RecommendationCardModel, position: Int)
    fun onProductCardClicked(model: RecommendationCardModel, position: Int)
    fun onProductCardThreeDotsClicked(model: RecommendationCardModel, position: Int)
}

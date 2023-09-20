package com.tokopedia.recommendation_widget_common.infinite.main

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface InfiniteRecommendationListener {
    fun onClickProductCard(recommendationItem: RecommendationItem)
    fun onImpressProductCard(recommendationItem: RecommendationItem)
}

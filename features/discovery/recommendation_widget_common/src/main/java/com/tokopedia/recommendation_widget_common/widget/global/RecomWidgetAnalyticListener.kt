package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface RecomWidgetAnalyticListener {
    fun onProductCardImpressed(recommendationItem: RecommendationItem, position: Int)
    fun onProductCardClicked(recommendationItem: RecommendationItem, position: Int)
}

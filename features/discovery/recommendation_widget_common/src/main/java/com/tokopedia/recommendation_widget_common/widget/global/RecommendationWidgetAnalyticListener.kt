package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface RecommendationWidgetAnalyticListener {
    fun onProductCardImpressed(recommendationItem: RecommendationItem) { }
    fun onProductCardClicked(recommendationItem: RecommendationItem) { }
    fun onHeaderSeeAllClicked(applink: String?) { }
    fun onViewAllCardClicked(applink: String?) { }
}

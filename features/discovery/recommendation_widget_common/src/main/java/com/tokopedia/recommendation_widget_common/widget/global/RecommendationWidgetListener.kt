package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface RecommendationWidgetListener {
    fun onProductClick(item: RecommendationItem): Boolean = false
}

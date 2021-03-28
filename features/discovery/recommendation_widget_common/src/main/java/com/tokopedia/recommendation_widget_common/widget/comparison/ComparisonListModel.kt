package com.tokopedia.recommendation_widget_common.widget.comparison

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class ComparisonListModel(
        val comparisonData: List<ComparisonModel>,
        val comparisonWidgetConfig: ComparisonWidgetConfig,
        val recommendationWidget: RecommendationWidget
) {
    fun getAnchorProduct(): ComparisonModel? {
        return comparisonData.getOrNull(0)
    }
}
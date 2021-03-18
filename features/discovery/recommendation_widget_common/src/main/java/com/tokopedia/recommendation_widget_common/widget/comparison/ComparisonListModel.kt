package com.tokopedia.recommendation_widget_common.widget.comparison

data class ComparisonListModel(
        val comparisonData: List<ComparisonModel>,
        val comparisonWidgetConfig: ComparisonWidgetConfig,
        val headerTitle: String,
        val seeMoreApplink: String = ""
)
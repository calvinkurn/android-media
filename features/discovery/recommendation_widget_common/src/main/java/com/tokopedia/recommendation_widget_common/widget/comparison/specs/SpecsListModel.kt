package com.tokopedia.recommendation_widget_common.widget.comparison.specs

data class SpecsListModel(
        val specs: List<SpecsModel>,
        val specsConfig: SpecsConfig,
        val currentRecommendationPosition: Int,
        val totalRecommendations: Int
)
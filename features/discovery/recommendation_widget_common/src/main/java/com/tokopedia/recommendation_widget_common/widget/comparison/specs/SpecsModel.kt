package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonColorConfig

data class SpecsModel(
    val specsTitle: String,
    val specsSummary: String,
    val bgDrawableRef: Int,
    val bgDrawableColorRef: Int,
    val colorConfig: ComparisonColorConfig,
)

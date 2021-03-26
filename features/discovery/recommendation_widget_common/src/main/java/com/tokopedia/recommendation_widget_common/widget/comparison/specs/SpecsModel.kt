package com.tokopedia.recommendation_widget_common.widget.comparison.specs

data class SpecsModel(
    val specsTitle: String,
    val specsSummary: String,
    val bgDrawableRef: Int,
    val bgDrawableColorRef: Int,
    val height: Int = 0
)
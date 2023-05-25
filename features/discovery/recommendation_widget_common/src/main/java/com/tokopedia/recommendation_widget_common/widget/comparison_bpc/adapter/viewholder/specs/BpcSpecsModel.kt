package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs

data class BpcSpecsModel(
    val specsTitle: String,
    val specsSummary: String,
    val specsSummaryBullet: List<BpcSpecsSummaryBullet>,
    val bgDrawableRef: Int,
    val bgDrawableColorRef: Int
)

data class BpcSpecsSummaryBullet(
    val specsSummary: String,
    val icon: String?
)

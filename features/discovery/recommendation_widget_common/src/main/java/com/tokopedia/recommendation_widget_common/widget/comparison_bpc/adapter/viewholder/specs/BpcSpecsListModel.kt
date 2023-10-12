package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs

/**
 * Created by Frenzel
 */
data class BpcSpecsListModel(
    val specs: List<BpcSpecsModel>,
    val specsConfig: BpcSpecsConfig,
    val currentRecommendationPosition: Int,
    val totalRecommendations: Int
)

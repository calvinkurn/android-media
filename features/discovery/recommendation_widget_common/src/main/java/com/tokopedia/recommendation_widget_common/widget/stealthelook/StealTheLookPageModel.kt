package com.tokopedia.recommendation_widget_common.widget.stealthelook

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class StealTheLookPageModel(
    val page: Int,
    val recomItemList: List<RecommendationItem>
)

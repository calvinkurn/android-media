package com.tokopedia.recommendation_widget_common.widget.stealthelook

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking.StealTheLookTracking

data class StealTheLookGridModel(
    val stylePosition: Int,
    val position: Int,
    val recommendationItem: RecommendationItem,
    val recommendationWidget: RecommendationWidget,
    val tracking: StealTheLookTracking?,
)

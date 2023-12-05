package com.tokopedia.recommendation_widget_common.widget.stealthelook

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking.StealTheLookTracking

data class StealTheLookStyleModel(
    val stylePosition: Int,
    val gridPositionMap: Map<RecommendationItem.GridPosition, StealTheLookGridModel>,
    val recommendationWidget: RecommendationWidget,
    val tracking: StealTheLookTracking?,
): ImpressHolder() {
    companion object {
        const val GRID_COUNT = 3
    }
}

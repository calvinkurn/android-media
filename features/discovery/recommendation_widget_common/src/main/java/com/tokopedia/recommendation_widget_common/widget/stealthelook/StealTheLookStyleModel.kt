package com.tokopedia.recommendation_widget_common.widget.stealthelook

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking.StealTheLookTracking

data class StealTheLookStyleModel(
    val stylePosition: Int,
    val grids: List<StealTheLookGridModel>,
    val tracking: StealTheLookTracking?,
): ImpressHolder() {
    companion object {
        const val GRID_COUNT = 3
    }
}

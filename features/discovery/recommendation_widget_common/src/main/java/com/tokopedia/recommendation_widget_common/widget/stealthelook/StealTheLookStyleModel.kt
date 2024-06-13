package com.tokopedia.recommendation_widget_common.widget.stealthelook

import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking.StealTheLookTracking

data class StealTheLookStyleModel(
    val stylePosition: Int,
    val grids: List<StealTheLookGridModel>,
    val tracking: StealTheLookTracking?,
    val appLogAdditionalParam: AppLogAdditionalParam,
): ImpressHolder() {
    companion object {
        const val GRID_COUNT = 3
    }
}

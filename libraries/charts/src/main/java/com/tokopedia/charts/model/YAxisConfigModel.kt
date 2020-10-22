package com.tokopedia.charts.model

import android.graphics.Typeface
import com.github.mikephil.charting.components.YAxis
import com.tokopedia.charts.common.utils.LabelFormatter

/**
 * Created By @ilhamsuaib on 25/06/20
 */

data class YAxisConfigModel(
        override val typeface: Typeface?,
        override val isEnabled: Boolean,
        override val isLabelEnabled: Boolean,
        override val isGridEnabled: Boolean,
        override val mLabelPosition: Int,
        override val axisMinimum: Float,
        override val labelFormatter: LabelFormatter,
        val spaceTop: Float,
        val labelCount: Int
): BaseAxisConfigModel {

    companion object {
        const val LABEL_OUTSIDE_CHART = 0
        const val LABEL_INSIDE_CHART = 1
    }

    fun getLabelPosition(): YAxis.YAxisLabelPosition {
        return if (mLabelPosition == LABEL_INSIDE_CHART) {
            YAxis.YAxisLabelPosition.INSIDE_CHART
        } else {
            YAxis.YAxisLabelPosition.OUTSIDE_CHART
        }
    }
}
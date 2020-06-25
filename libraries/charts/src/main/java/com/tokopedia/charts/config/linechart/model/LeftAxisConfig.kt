package com.tokopedia.charts.config.linechart.model

import android.graphics.Color
import android.graphics.Typeface
import com.github.mikephil.charting.components.YAxis

/**
 * Created By @ilhamsuaib on 25/06/20
 */

data class LeftAxisConfig(
        val textColor: Int = Color.BLACK,
        val typeface: Typeface? = null,
        val position: Int = OUTSIDE_CHART,
        val isDrawGridLines: Boolean = true
) {
    companion object {
        const val OUTSIDE_CHART = 0
        const val INSIDE_CHART = 1
    }

    fun getPosition(): YAxis.YAxisLabelPosition {
        return if (position == OUTSIDE_CHART) {
            YAxis.YAxisLabelPosition.OUTSIDE_CHART
        } else {
            YAxis.YAxisLabelPosition.OUTSIDE_CHART
        }
    }
}
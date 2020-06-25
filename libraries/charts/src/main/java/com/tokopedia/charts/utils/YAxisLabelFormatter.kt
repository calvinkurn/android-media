package com.tokopedia.charts.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import com.tokopedia.charts.model.YAxisLabel
import kotlin.math.abs

/**
 * Created By @ilhamsuaib on 24/06/20
 */

class YAxisLabelFormatter(private val labels: List<YAxisLabel>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return getClosestNumber(value)
    }

    private fun getClosestNumber(value: Float): String {
        if (labels.isEmpty()) return ""
        var distance = abs(labels[0].yValue - value)
        var idx = 0
        labels.forEachIndexed { i, label ->
            val cdistance = abs(label.yValue - value)
            if (cdistance < distance) {
                idx = i
                distance = cdistance
            }
        }
        return labels[idx].yLabel
    }
}
package com.tokopedia.charts.utils

import com.github.mikephil.charting.formatter.ValueFormatter

/**
 * Created By @ilhamsuaib on 24/06/20
 */

class XAxisLabelFormatter(private val xLabels: List<String>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return try {
            xLabels[value.toInt()]
        } catch (e: IndexOutOfBoundsException) {
            ""
        }
    }
}
package com.tokopedia.charts.common.utils

import com.github.mikephil.charting.formatter.ValueFormatter

/**
 * Created By @ilhamsuaib on 24/06/20
 */

class DefaultXAxisLabelFormatter(private val xLabels: List<String>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return try {
            xLabels[value.toInt()]
        } catch (e: IndexOutOfBoundsException) {
            value.toInt().toString()
        }
    }
}
package com.tokopedia.sellerhomecommon.utils

import com.tokopedia.charts.common.utils.LabelFormatter

/**
 * Created By @ilhamsuaib on 14/08/20
 */

class ChartXAxisLabelFormatter(private val xLabels: List<String>) : LabelFormatter {

    override fun getAxisLabel(value: Float): String {
        return try {
            xLabels[value.toInt()]
        } catch (e: IndexOutOfBoundsException) {
            value.toInt().toString()
        }
    }
}
package com.tokopedia.topads.dashboard.data.utils

import com.tokopedia.charts.common.utils.LabelFormatter

/**
 * Created by Pika on 20/10/20.
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
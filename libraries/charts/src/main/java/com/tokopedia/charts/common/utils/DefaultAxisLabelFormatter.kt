package com.tokopedia.charts.common.utils

/**
 * Created By @ilhamsuaib on 13/08/20
 */

class DefaultAxisLabelFormatter : LabelFormatter {

    override fun getAxisLabel(value: Float): String {
        return value.toInt().toString()
    }
}
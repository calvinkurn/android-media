package com.tokopedia.charts.model

import com.tokopedia.charts.common.ChartColor

/**
 * Created By @ilhamsuaib on 13/07/20
 */

data class BarChartMetricValue(
    val value: Float,
    val yLabel: String,
    val xLabel: String,
    val barColor: String = ChartColor.DMS_DEFAULT_BAR_COLOR
)

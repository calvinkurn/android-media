package com.tokopedia.charts.model

import com.tokopedia.charts.common.ChartColor

/**
 * Created By @ilhamsuaib on 13/07/20
 */

data class BarChartMetric(
    val title: String = "",
    val barHexColor: String = ChartColor.DMS_DEFAULT_BAR_COLOR,
    val values: List<BarChartMetricValue> = emptyList()
)
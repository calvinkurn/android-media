package com.tokopedia.charts.model

/**
 * Created By @ilhamsuaib on 13/07/20
 */

data class BarChartMetric(
        val title: String = "",
        val barHexColor: String = "#6DB770",
        val values: List<BarChartMetricValue> = emptyList()
)
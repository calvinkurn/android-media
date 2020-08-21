package com.tokopedia.charts.model

/**
 * Created By @ilhamsuaib on 13/07/20
 */

data class BarChartData(
        val yAxis: List<AxisLabel>,
        val xAxisLabels: List<AxisLabel>,
        val metrics: List<BarChartMetric>
)
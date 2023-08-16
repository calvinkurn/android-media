package com.tokopedia.charts.model

data class StackedBarChartData(
    val yAxis: List<AxisLabel>,
    val xAxisLabels: List<AxisLabel>,
    val metrics: List<StackedBarChartMetric>
)

package com.tokopedia.charts.model

import com.tokopedia.charts.common.ChartColor

data class StackedBarChartMetric(
    val title: String = "",
    val barHexColor: String = ChartColor.DMS_DEFAULT_BAR_COLOR,
    val values: List<StackedBarChartMetricValue> = emptyList()
)

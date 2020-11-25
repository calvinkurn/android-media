package com.tokopedia.charts.model

/**
 * Created By @ilhamsuaib on 16/07/20
 */

data class LineChartData(
        val chartEntry: List<LineChartEntry>,
        val yAxisLabel: List<AxisLabel>,
        val config: LineChartEntryConfigModel
)
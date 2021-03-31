package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 09/07/20
 */

data class BarChartDataUiModel(
        override val dataKey: String = "",
        override var error: String = "",
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = false,
        val chartData: BarChartUiModel = BarChartUiModel()
) : BaseDataUiModel {
    override fun shouldRemove(): Boolean {
        return chartData.yAxis.all { it.value == 0 }
    }
}

data class BarChartUiModel(
        val metrics: List<BarChartMetricsUiModel> = emptyList(),
        val xAxis: List<BarChartAxisUiModel> = emptyList(),
        val yAxis: List<BarChartAxisUiModel> = emptyList(),
        val summary: ChartSummaryUiModel = ChartSummaryUiModel()
)

data class BarChartMetricsUiModel(
        val value: List<BarChartAxisUiModel> = emptyList(),
        val title: String = "",
        val barHexColor: String = ""
)

data class BarChartAxisUiModel(
        val value: Int = 0,
        val valueFmt: String = ""
)
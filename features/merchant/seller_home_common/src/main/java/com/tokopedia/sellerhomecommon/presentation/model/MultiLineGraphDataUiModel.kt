package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.sellerhomecommon.common.const.MetricsType

/**
 * Created By @ilhamsuaib on 26/10/20
 */

data class MultiLineGraphDataUiModel(
        override var dataKey: String = "",
        override var error: String = "",
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = false,
        val metrics: List<MultiLineMetricUiModel> = emptyList()
) : BaseDataUiModel {
    override fun shouldRemove(): Boolean {
        return metrics.all { it.yAxis.all { it.yValue == 0f } }
    }
}

data class MultiLineMetricUiModel(
        val isError: Boolean = true,
        val errorMsg: String = "",
        val type: String = MetricsType.UNKNOWN,
        val summary: MetricsSummaryUiModel = MetricsSummaryUiModel(),
        val yAxis: List<YAxisUiModel> = emptyList(),
        val linePeriod: LinePeriodUiModel = LinePeriodUiModel(),
        val isEmpty: Boolean = false,
        var isSelected: Boolean = false
)

data class MetricsSummaryUiModel(
        val title: String = "",
        val valueFmt: String = "",
        val state: String = "",
        val description: String = "",
        val lineColor: String = ""
)

data class LinePeriodUiModel(
        val currentPeriod: List<XYAxisUiModel> = emptyList(),
        val lastPeriod: List<XYAxisUiModel> = emptyList()
)
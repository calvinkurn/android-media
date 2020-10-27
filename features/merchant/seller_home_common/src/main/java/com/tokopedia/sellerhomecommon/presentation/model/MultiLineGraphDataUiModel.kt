package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.sellerhomecommon.common.const.MetricsType

/**
 * Created By @ilhamsuaib on 26/10/20
 */

data class MultiLineGraphDataUiModel(
        override val dataKey: String = "",
        override var error: String = "",
        val metrics: List<MultiLineMetricUiModel> = emptyList()
) : BaseDataUiModel

data class MultiLineMetricUiModel(
        val errorMsg: String = "",
        val type: String = MetricsType.UNKNOWN,
        val summary: MetricsSummaryUiModel = MetricsSummaryUiModel(),
        val yAxis: List<MultiLineYAxisUiModel> = emptyList(),
        val linePeriod: LinePeriodUiModel = LinePeriodUiModel()
)

data class MetricsSummaryUiModel(
        val title: String = "",
        val valueFmt: String = "",
        val state: String = "",
        val description: String = "",
        val lineColor: String = ""
)

data class MultiLineYAxisUiModel(
        val yValue: Float = 0f,
        val yLabel: String = ""
)

data class LinePeriodUiModel(
        val currentPeriod: List<PeriodAxisUiModel> = emptyList(),
        val lastPeriod: List<PeriodAxisUiModel> = emptyList()
)

data class PeriodAxisUiModel(
        val yValue: Float = 0f,
        val yLabelFmt: String = "",
        val xLabelFmt: String = ""
)
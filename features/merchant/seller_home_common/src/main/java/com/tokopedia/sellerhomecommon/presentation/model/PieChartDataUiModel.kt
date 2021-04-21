package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 06/07/20
 */

class PieChartDataUiModel(
        override val dataKey: String = "",
        override var error: String = "",
        val data: PieChartUiModel = PieChartUiModel(),
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = false
) : BaseDataUiModel {
    override fun shouldRemove(): Boolean {
        return data.item.all { it.value == 0 }
    }
}

data class PieChartUiModel(
        val item: List<PieChartItemUiModel> = emptyList(),
        val summary: ChartSummaryUiModel = ChartSummaryUiModel()
)

data class PieChartItemUiModel(
        val color: String = "",
        val legend: String = "",
        val percentage: Int = 0,
        val percentageFmt: String = "",
        val value: Int = 0,
        val valueFmt: String = ""
)
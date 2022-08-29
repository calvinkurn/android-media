package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 06/07/20
 */

class PieChartDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = false,
    override val lastUpdated: LastUpdatedUiModel = LastUpdatedUiModel(),
    val data: PieChartUiModel = PieChartUiModel()
) : BaseDataUiModel, LastUpdatedDataInterface {

    override fun isWidgetEmpty(): Boolean {
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
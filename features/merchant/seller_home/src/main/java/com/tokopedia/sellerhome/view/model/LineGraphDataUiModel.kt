package com.tokopedia.sellerhome.view.model

/**
 * Created By @ilhamsuaib on 2020-01-27
 */

data class LineGraphDataUiModel(
        val dataKey: String = "",
        val description: String = "",
        val error: String = "",
        val header: String = "",
        val list: List<XYAxisUiModel> = emptyList(),
        val yLabels: List<XYAxisUiModel> = emptyList()
) : BaseDataUiModel

data class XYAxisUiModel(
        val xLabel: String,
        val yLabel: String,
        val yVal: Int
)
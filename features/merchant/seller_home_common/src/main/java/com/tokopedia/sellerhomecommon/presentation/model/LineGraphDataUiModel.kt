package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class LineGraphDataUiModel(
        override val dataKey: String = "",
        val description: String = "",
        override var error: String = "",
        val header: String = "",
        val list: List<XYAxisUiModel> = emptyList(),
        val yLabels: List<XYAxisUiModel> = emptyList()
) : BaseDataUiModel

data class XYAxisUiModel(
        val xLabel: String,
        val yLabel: String,
        val yVal: Float
)
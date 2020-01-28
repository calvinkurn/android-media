package com.tokopedia.sellerhome.view.model

/**
 * Created By @ilhamsuaib on 2020-01-27
 */

data class LineGraphDataUiModel(
        val dataKey: String,
        val description: String,
        val error: String,
        val header: String,
        val list: List<XYAxisUiModel>,
        val yLabels: List<XYAxisUiModel>
)

data class XYAxisUiModel(
        val xLabel: String,
        val yLabel: String,
        val yVal: Long
)
package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class LineGraphDataUiModel(
        override var dataKey: String = "",
        val description: String = "",
        override var error: String = "",
        val header: String = "",
        val list: List<XYAxisUiModel> = emptyList(),
        val yLabels: List<XYAxisUiModel> = emptyList(),
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = false
) : BaseDataUiModel {
    override fun shouldRemove(): Boolean {
        return list.all { it.yVal == 0f }
    }
}
package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class LineGraphDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = false,
    override val lastUpdated: Long = 0,
    val description: String = "",
    val header: String = "",
    val list: List<XYAxisUiModel> = emptyList(),
    val yLabels: List<XYAxisUiModel> = emptyList()
) : BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return list.all { it.yVal == 0f }
    }
}
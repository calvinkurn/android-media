package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 19/05/20
 */

data class CardDataUiModel(
        override var dataKey: String = "",
        val description: String = "",
        val state: String = "",
        val value: String = "",
        override var error: String = "",
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = false,
        var previousValue: String? = null
): BaseDataUiModel {
    override fun shouldRemove(): Boolean {
        return value.filterIndexed { index, c ->
            (c == '.' && value.getOrNull(index - 1)?.isDigit() == true) || c.isDigit()
        }.toFloat() == 0f
    }
}
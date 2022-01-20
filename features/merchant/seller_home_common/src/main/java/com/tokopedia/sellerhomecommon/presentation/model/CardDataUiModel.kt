package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.extensions.view.toFloatOrZero

/**
 * Created By @ilhamsuaib on 19/05/20
 */

data class CardDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = false,
    val description: String = "",
    val state: State = State.NORMAL,
    val value: String = "",
    var previousValue: String? = null
) : BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return value.filterIndexed { index, c ->
            (c == '.' && value.getOrNull(index - 1)?.isDigit() == true) || c.isDigit()
        }.toFloatOrZero() == 0f
    }

    enum class State {
        NORMAL, WARNING, DANGER
    }
}
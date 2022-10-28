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
    override val lastUpdated: LastUpdatedUiModel = LastUpdatedUiModel(),
    val description: String = "",
    val secondaryDescription: String = "",
    val state: State = State.NORMAL,
    val value: String = "",
    var previousValue: String? = null,
    val badgeImageUrl: String = ""
) : BaseDataUiModel, LastUpdatedDataInterface {

    override fun isWidgetEmpty(): Boolean {
        return value.filterIndexed { index, c ->
            (c == '.' && value.getOrNull(index - 1)?.isDigit() == true) || c.isDigit()
        }.toFloatOrZero() == 0f
    }

    enum class State {
        NORMAL, WARNING, DANGER, GOOD, GOOD_PLUS, WARNING_PLUS, DANGER_PLUS
    }
}
package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toFloatOrZero

/**
 * Created By @ilhamsuaib on 19/05/20
 */

data class CardDataUiModel(
    override var dataKey: String = String.EMPTY,
    override var error: String = String.EMPTY,
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = false,
    override val lastUpdated: LastUpdatedUiModel = LastUpdatedUiModel(),
    val description: String = String.EMPTY,
    val secondaryDescription: String = String.EMPTY,
    val state: State = State.NORMAL,
    val value: String = String.EMPTY,
    var previousValue: String? = null,
    val badgeImageUrl: String = String.EMPTY,
    val appLink: String = String.EMPTY
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
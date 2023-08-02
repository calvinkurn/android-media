package com.tokopedia.sellerpersona.view.compose.model.uievent

import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

sealed class QuestionnaireUiEvent {
    data class OnSubmitted(
        val persona: String = "", val throwable: Throwable? = null
    ) : QuestionnaireUiEvent()

    data class OnMultipleOptionChecked(
        val option: BaseOptionUiModel.QuestionOptionSingleUiModel,
        val isChecked: Boolean
    ) : QuestionnaireUiEvent()

    object ClickNext : QuestionnaireUiEvent()
    object ClickPrevious : QuestionnaireUiEvent()
}
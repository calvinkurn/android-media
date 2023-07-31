package com.tokopedia.sellerpersona.view.compose.model.uievent

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

sealed class QuestionnaireUiEvent {
    data class OnSubmitted(
        val persona: String = "", val throwable: Throwable? = null
    ) : QuestionnaireUiEvent()

    object ClickNext : QuestionnaireUiEvent()
    object ClickPrevious : QuestionnaireUiEvent()
}
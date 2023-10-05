package com.tokopedia.sellerpersona.view.compose.model.uievent

import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

sealed interface QuestionnaireUserEvent {
    data class OnOptionItemSelected(
        val pagePosition: Int,
        val option: BaseOptionUiModel,
        val isChecked: Boolean
    ) : QuestionnaireUserEvent

    data class OnPagerSwipe(val page: Int) : QuestionnaireUserEvent
    object ClickNext : QuestionnaireUserEvent
    object ClickPrevious : QuestionnaireUserEvent
    object FetchQuestionnaire : QuestionnaireUserEvent
}
package com.tokopedia.sellerpersona.view.compose.model.uievent

import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

sealed class ResultUiEvent {
    data class OnSwitchCheckChanged(val isChecked: Boolean) : ResultUiEvent()
    data class ApplyChanges(val persona: String, val isActive: Boolean) : ResultUiEvent()
    data class SelectPersona(val currentPersona: String) : ResultUiEvent()
    class FetchPersonaData(val arguments: PersonaArgsUiModel) : ResultUiEvent()
    object Reload : ResultUiEvent()
    object RetakeQuiz : ResultUiEvent()
    object OnResultImpressedEvent : ResultUiEvent()
}
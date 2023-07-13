package com.tokopedia.sellerpersona.view.compose.model

import com.tokopedia.sellerpersona.view.model.PersonaStatus

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

sealed class ResultUiEvent {
    data class TogglePersona(val isEnabled: Boolean) : ResultUiEvent()
    data class ApplyChanges(val status: PersonaStatus) : ResultUiEvent()
    object RetakeQuiz : ResultUiEvent()
}
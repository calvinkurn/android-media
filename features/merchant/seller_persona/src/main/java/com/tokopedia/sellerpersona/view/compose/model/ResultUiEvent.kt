package com.tokopedia.sellerpersona.view.compose.model

import com.tokopedia.sellerpersona.view.model.PersonaStatus

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

sealed class ResultUiEvent {
    data class TogglePersona(val isEnabled: Boolean) : ResultUiEvent()
    data class CheckChanged(val isChecked: Boolean) : ResultUiEvent()
    object ApplyChanges : ResultUiEvent()
    object Reload : ResultUiEvent()
    object RetakeQuiz : ResultUiEvent()
    object None : ResultUiEvent()
}
package com.tokopedia.sellerpersona.view.compose.model.uievent

import com.tokopedia.sellerpersona.view.model.PersonaStatus

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

sealed class ResultUiEvent {
    data class OnPersonaStatusChanged(
        val personaStatus: PersonaStatus = PersonaStatus.INACTIVE,
        val throwable: Throwable? = null
    ) : ResultUiEvent() {
        fun isSuccess(): Boolean {
            return throwable == null
        }
    }

    data class CheckChanged(val isChecked: Boolean) : ResultUiEvent()
    data class ApplyChanges(val persona: String, val isActive: Boolean) : ResultUiEvent()
    data class SelectPersona(val currentPersona: String) : ResultUiEvent()
    object Reload : ResultUiEvent()
    object RetakeQuiz : ResultUiEvent()
}
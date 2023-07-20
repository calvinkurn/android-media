package com.tokopedia.sellerpersona.view.compose.model

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
    object Reload : ResultUiEvent()
    object RetakeQuiz : ResultUiEvent()
    object None : ResultUiEvent()
}
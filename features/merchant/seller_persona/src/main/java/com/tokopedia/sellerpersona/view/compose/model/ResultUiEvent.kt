package com.tokopedia.sellerpersona.view.compose.model

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

sealed class ResultUiEvent {
    data class OnPersonaStatusChanged(
        val isSuccess: Boolean = false,
        val throwable: Throwable? = null
    ) : ResultUiEvent()

    data class CheckChanged(val isChecked: Boolean) : ResultUiEvent()
    data class ApplyChanges(val persona: String, val isActive: Boolean) : ResultUiEvent()
    object Reload : ResultUiEvent()
    object RetakeQuiz : ResultUiEvent()
    object None : ResultUiEvent()
}
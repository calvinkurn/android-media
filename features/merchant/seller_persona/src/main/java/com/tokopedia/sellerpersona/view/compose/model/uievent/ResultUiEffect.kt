package com.tokopedia.sellerpersona.view.compose.model.uievent

import com.tokopedia.sellerpersona.view.model.PersonaStatus

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

sealed class ResultUiEffect {
    data class OnPersonaStatusChanged(
        val personaStatus: PersonaStatus = PersonaStatus.INACTIVE,
        val throwable: Throwable? = null
    ) : ResultUiEffect() {
        fun isSuccess(): Boolean = throwable == null
    }

    //navigation
    data class NavigateToSelectPersona(val currentPersona: String) : ResultUiEffect()
    object NavigateToQuestionnaire : ResultUiEffect()

    //analytics
    data class SendClickApplyTracking(val persona: String, val isActive: Boolean) : ResultUiEffect()
    object SendImpressionResultTracking : ResultUiEffect()
    object SendSwitchCheckedChangedTracking : ResultUiEffect()
}
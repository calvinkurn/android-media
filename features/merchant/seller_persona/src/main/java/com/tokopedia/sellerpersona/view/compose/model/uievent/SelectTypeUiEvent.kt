package com.tokopedia.sellerpersona.view.compose.model.uievent

import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 25/07/23.
 */

sealed class SelectTypeUiEvent {
    data class ClickPersonaCard(val persona: PersonaUiModel) : SelectTypeUiEvent()
    object ClickSelectButton : SelectTypeUiEvent()
    object Reload : SelectTypeUiEvent()
    data class CloseThePage(val persona: String) : SelectTypeUiEvent()
    data class OnPersonaChanged(
        val persona: String,
        val exception: Exception? = null
    ) : SelectTypeUiEvent()
    data class SetBackPressedEnabled(val enabled: Boolean) : SelectTypeUiEvent()
}
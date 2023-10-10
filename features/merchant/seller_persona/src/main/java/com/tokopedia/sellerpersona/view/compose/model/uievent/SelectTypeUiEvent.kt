package com.tokopedia.sellerpersona.view.compose.model.uievent

import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 25/07/23.
 */

sealed class SelectTypeUiEvent {
    class FetchPersonaList(val arguments: PersonaArgsUiModel) : SelectTypeUiEvent()
    data class ClickPersonaCard(val personaName: String) : SelectTypeUiEvent()
    object ClickSubmitButton : SelectTypeUiEvent()
    object Reload : SelectTypeUiEvent()
}
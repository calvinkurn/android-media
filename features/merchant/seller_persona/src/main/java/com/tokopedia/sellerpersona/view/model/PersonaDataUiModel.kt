package com.tokopedia.sellerpersona.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

data class PersonaDataUiModel(
    val persona: String = String.EMPTY,
    val personaStatus: PersonaStatus = PersonaStatus.UNDEFINED,
    val personaData: PersonaUiModel = PersonaUiModel()
)
package com.tokopedia.sellerpersona.view.compose.model.state

import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel

/**
 * Created by @ilhamsuaib on 18/07/23.
 */

data class PersonaResultState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val data: PersonaDataUiModel = PersonaDataUiModel()
)
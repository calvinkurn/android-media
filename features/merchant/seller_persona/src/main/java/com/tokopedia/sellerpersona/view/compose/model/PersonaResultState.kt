package com.tokopedia.sellerpersona.view.compose.model

import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel

/**
 * Created by @ilhamsuaib on 18/07/23.
 */

data class PersonaResultState(
    val isLoading: Boolean = true,
    val data: PersonaDataUiModel = PersonaDataUiModel(),
    val error: Throwable? = null,
    val args: ResultArgsUiModel = ResultArgsUiModel()
)
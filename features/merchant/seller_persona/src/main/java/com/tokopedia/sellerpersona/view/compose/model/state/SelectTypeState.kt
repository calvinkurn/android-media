package com.tokopedia.sellerpersona.view.compose.model.state

import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 25/07/23.
 */

data class SelectTypeState(
    val isLoading: Boolean = false,
    val exception: Exception? = null,
    val data: Data = Data()
) {
    data class Data(
        val personaList: List<PersonaUiModel> = emptyList(),
        val args: PersonaArgsUiModel = PersonaArgsUiModel()
    ) {

    }
}
package com.tokopedia.sellerpersona.view.compose.model.state

import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel

/**
 * Created by @ilhamsuaib on 18/07/23.
 */

data class PersonaResultState(
    val state: State = State.Loading,
    val data: PersonaDataUiModel = PersonaDataUiModel()
) {

    sealed class State {
        object Loading : State()
        data class Error(val throwable: Throwable) : State()
        object Success : State()
    }
}
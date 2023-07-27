package com.tokopedia.sellerpersona.view.compose.model.state

import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 25/07/23.
 */

data class SelectTypeState(
    val state: State = State.Loading,
    val data: Data = Data()
) {
    data class Data(
        val personaList: List<PersonaUiModel> = emptyList(),
        val args: PersonaArgsUiModel = PersonaArgsUiModel(),
        val ui: Ui = Ui()
    )

    sealed class State {
        object Loading : State()
        object Success : State()
        data class Error(val e: Exception) : State()
    }

    data class Ui(
        val selectedIndex: Int = -1,
        val isSelectButtonLoading: Boolean = false
    )
}
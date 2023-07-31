package com.tokopedia.sellerpersona.view.compose.model.state

import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

data class QuestionnaireState(
    val state: State = State.Loading,
    val data: Data = Data()
) {

    data class Data(
        val questionnaireList: List<QuestionnairePagerUiModel> = emptyList()
    )

    sealed class State {
        object Loading : State()
        object Success : State()
        object Error : State()
    }
}
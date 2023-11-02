package com.tokopedia.sellerpersona.view.compose.model.state

import com.tokopedia.sellerpersona.view.model.QuestionnaireDataUiModel

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

sealed class QuestionnaireState {
    object Loading : QuestionnaireState()
    object Error : QuestionnaireState()
    data class Success(
        val data: QuestionnaireDataUiModel
    ) : QuestionnaireState()
}
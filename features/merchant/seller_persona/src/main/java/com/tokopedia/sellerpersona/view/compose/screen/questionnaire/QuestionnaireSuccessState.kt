package com.tokopedia.sellerpersona.view.compose.screen.questionnaire

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.sellerpersona.view.compose.model.state.QuestionnaireState
import com.tokopedia.sellerpersona.view.compose.model.uievent.QuestionnaireUiEvent

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

@Composable
fun QuestionnaireSuccessState(
    data: QuestionnaireState.Data,
    event: (QuestionnaireUiEvent) -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

    }
}
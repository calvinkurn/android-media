package com.tokopedia.sellerpersona.view.compose.screen.questionnaire

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.compose.component.ErrorStateComponent
import com.tokopedia.sellerpersona.view.compose.model.uievent.QuestionnaireUserEvent

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

@Composable
fun QuestionnaireErrorState(onEvent: (QuestionnaireUserEvent) -> Unit) {
    ErrorStateComponent(
        actionText = stringResource(id = R.string.sp_reload),
        title = stringResource(id = R.string.sp_common_global_error_title),
        onActionClicked = {
            onEvent(QuestionnaireUserEvent.FetchQuestionnaire)
        }
    )
}
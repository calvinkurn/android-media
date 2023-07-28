package com.tokopedia.sellerpersona.view.compose.screen.personaresult

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.compose.component.ErrorStateComponent
import com.tokopedia.sellerpersona.view.compose.model.uievent.ResultUiEvent

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

@Composable
fun ResultErrorState(onEvent: (ResultUiEvent) -> Unit) {
    ErrorStateComponent(
        actionText = stringResource(id = R.string.sp_reload),
        title = stringResource(id = R.string.sp_common_global_error_title),
        onActionClicked = {
            onEvent(ResultUiEvent.Reload)
        }
    )
}
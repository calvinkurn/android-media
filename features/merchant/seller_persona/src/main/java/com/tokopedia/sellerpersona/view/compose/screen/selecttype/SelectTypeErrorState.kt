package com.tokopedia.sellerpersona.view.compose.screen.selecttype

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.compose.component.ErrorStateComponent
import com.tokopedia.sellerpersona.view.compose.model.uievent.SelectTypeUiEvent

/**
 * Created by @ilhamsuaib on 26/07/23.
 */

@Composable
internal fun SelectTypeErrorState(onEvent: (SelectTypeUiEvent) -> Unit) {
    ErrorStateComponent(
        actionText = stringResource(id = R.string.sp_reload),
        title = stringResource(id = R.string.sp_common_global_error_title),
        onActionClicked = {
            onEvent(SelectTypeUiEvent.Reload)
        }
    )
}
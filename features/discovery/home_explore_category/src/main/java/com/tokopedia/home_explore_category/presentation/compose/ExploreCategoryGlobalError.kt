package com.tokopedia.home_explore_category.presentation.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.globalerror.compose.NestGlobalError
import com.tokopedia.globalerror.compose.NestGlobalErrorType
import com.tokopedia.home_explore_category.R
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiEvent

@Composable
fun ExploreCategoryGlobalError(
    globalErrorType: NestGlobalErrorType,
    uiEvent: (ExploreCategoryUiEvent) -> Unit = {}
) {
    NestGlobalError(
        type = globalErrorType,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 16.dp),
        onClickAction = {
            uiEvent(ExploreCategoryUiEvent.OnPrimaryButtonErrorClicked)
        },
        onClickSecondaryAction = {
            uiEvent(ExploreCategoryUiEvent.OnSecondaryButtonErrorClicked)
        },
        secondaryActionText = if (globalErrorType == NestGlobalErrorType.NoConnection) {
            stringResource(id = R.string.secondary_title_go_to_setting)
        } else {
            null
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ExploreCategoryGlobalErrorPreview() {
    ExploreCategoryGlobalError(NestGlobalErrorType.ServerError)
}

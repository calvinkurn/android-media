package com.tokopedia.people.views.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestLocalLoad
import com.tokopedia.people.R

@Composable
fun FollowListErrorScreen(
    isLoading: Boolean,
    onRefreshButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onRefreshClicked = remember<(Boolean) -> Unit>(onRefreshButtonClicked) {
        { onRefreshButtonClicked() }
    }

    NestLocalLoad(
        title = stringResource(R.string.up_common_failed_title),
        description = stringResource(R.string.up_common_failed_title),
        isLoading = isLoading,
        onRefreshButtonClicked = onRefreshClicked,
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp)
    )
}

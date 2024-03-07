package com.tokopedia.tokopedianow.common.analytics

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun ListItemImpression(key: String, lazyListState: LazyListState, onImpressItemView: () -> Unit) {
    val isItemWithKeyInView by remember {
        derivedStateOf {
            lazyListState.layoutInfo
                .visibleItemsInfo
                .any { it.key == key }
        }
    }

    if (isItemWithKeyInView) {
        LaunchedEffect(Unit) {
            onImpressItemView()
        }
    }
}

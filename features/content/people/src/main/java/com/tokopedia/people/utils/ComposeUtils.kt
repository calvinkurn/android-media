package com.tokopedia.people.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
internal fun LazyListState.onLoadMore(
    buffer: Int = 0,
    onShouldLoadMore: () -> Unit
) {
    val shouldLoadMore by remember {
        derivedStateOf {
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) return@derivedStateOf true

            val lastVisibleItem = visibleItems.last()

            // if all items appeared on screen
            if (firstVisibleItemIndex == 0 && lastVisibleItem.index == layoutInfo.totalItemsCount - 1) return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1 - buffer
        }
    }

    LaunchedEffect(shouldLoadMore, layoutInfo.totalItemsCount) {
        if (shouldLoadMore) onShouldLoadMore()
    }
}

private const val PREFIX_ID = "com.tokopedia.tkpd:id"

@Composable
internal fun Modifier.resId(id: String) = then(
    Modifier.testTag("$PREFIX_ID/$id")
)

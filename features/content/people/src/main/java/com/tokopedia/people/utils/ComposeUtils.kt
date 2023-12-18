package com.tokopedia.people.utils

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

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

internal object LoadingModel

@Suppress("UNCHECKED_CAST")
internal inline fun <T : Any> LazyListScope.itemsLoadMore(
    items: List<T>,
    hasNextPage: Boolean = false,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline loadingContent: @Composable LazyItemScope.() -> Unit = {},
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    val newItems = if (hasNextPage) {
        buildList<Any> {
            addAll(items)
            add(LoadingModel)
        }
    } else {
        items
    }

    items(
        count = newItems.size,
        key = if (key != null) {
            { index: Int ->
                when (val item = newItems[index]) {
                    LoadingModel -> "loading_key"
                    else -> key(item as T)
                }
            }
        } else {
            null
        },
        contentType = { index: Int ->
            when (val item = newItems[index]) {
                LoadingModel -> LoadingModel
                else -> contentType(item as T)
            }
        }
    ) {
        when (val item = newItems[it]) {
            LoadingModel -> loadingContent()
            else -> itemContent(item as T)
        }
    }
}

package com.tokopedia.people.utils

import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

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

class RecompositionCounter(var value: Int)

@Composable
inline fun LogCompositions(tag: String, msg: String) {
    val context = LocalContext.current
    val isDebuggable = if (context !is AppCompatActivity) {
        false
    } else {
        val appInfo = context.applicationInfo
        (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    if (isDebuggable) {
        val recompositionCounter = remember { RecompositionCounter(0) }

        Log.d(tag, "$msg ${recompositionCounter.value} $currentRecomposeScope")
        recompositionCounter.value++
    }
}

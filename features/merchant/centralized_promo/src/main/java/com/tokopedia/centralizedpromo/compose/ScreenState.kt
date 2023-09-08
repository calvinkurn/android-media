package com.tokopedia.centralizedpromo.compose

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@ExperimentalMaterialApi
@Stable
class ScreenState(
    val lazyGridState: LazyGridState,
    val lazyListState: LazyListState,
    val pullRefreshState: PullRefreshState,
    val context: Context
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberScreenState(
    pullRefreshState: PullRefreshState,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    lazyListState: LazyListState = rememberLazyListState(),
    context: Context = LocalContext.current
): ScreenState = remember(
    pullRefreshState,
    lazyGridState,
    lazyListState,
    context
) {
    ScreenState(
        pullRefreshState = pullRefreshState,
        lazyGridState = lazyGridState,
        lazyListState = lazyListState,
        context = context
    )
}
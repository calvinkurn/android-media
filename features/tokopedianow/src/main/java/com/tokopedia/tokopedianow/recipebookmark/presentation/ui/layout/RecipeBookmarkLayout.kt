package com.tokopedia.tokopedianow.recipebookmark.presentation.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.ui.item.TokoNowLoadMoreProgressItem
import com.tokopedia.tokopedianow.common.ui.layout.TokoNowGlobalErrorLayout
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics
import com.tokopedia.tokopedianow.recipebookmark.presentation.uimodel.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.uimodel.RecipeShimmeringUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.item.RecipeBookmarkItem
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.item.RecipeBookmarkShimmeringItem
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkEvent
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkState
import kotlinx.coroutines.launch

private const val FIRST_ITEM_INDEX = 0

@Composable
fun RecipeBookmarkLayout(
    state: RecipeBookmarkState,
    analytics: RecipeBookmarkAnalytics,
    onEvent: (RecipeBookmarkEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NestTheme.colors.NN._0)
    ) {
        NestHeader(
            modifier = Modifier.fillMaxWidth(),
            type = NestHeaderType.SingleLine(
                title = stringResource(
                    R.string.tokopedianow_recipe_bookmark_title_page
                ),
                onBackClicked = {
                    onEvent(RecipeBookmarkEvent.PressBackButton)
                    analytics.clickBackButton()
                }
            )
        )

        when (state) {
            is RecipeBookmarkState.Loading -> RecipeBookmarkShimmeringLayout()
            is RecipeBookmarkState.Show -> RecipeBookmarkList(state, onEvent, analytics)
            is RecipeBookmarkState.Empty -> RecipeBookmarkEmptyLayout()
            is RecipeBookmarkState.Error -> {
                RecipeBookmarkErrorLayout(state) {
                    onEvent(RecipeBookmarkEvent.ClickEmptyStateActionButton(it))
                }
            }
        }
    }
}

@Composable
fun RecipeBookmarkList(
    state: RecipeBookmarkState.Show,
    onEvent: (RecipeBookmarkEvent) -> Unit,
    analytics: RecipeBookmarkAnalytics
) {
    val items = remember { state.items }
    val scrollToTop = state.scrollToTop

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val isScrolledToEnd by remember {
        derivedStateOf {
            lazyListState.isScrolledToEnd()
        }
    }

    LaunchedEffect(isScrolledToEnd) {
        onEvent(RecipeBookmarkEvent.LoadMoreRecipeBookmarkList(isScrolledToEnd))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
        state = lazyListState
    ) {
        itemsIndexed(items = items, key = { index, item -> item.getId(index) }) { index, item ->
            when (item) {
                is RecipeUiModel -> RecipeBookmarkItem(
                    position = index,
                    recipe = item,
                    analytics = analytics,
                    lazyListState = lazyListState,
                    onEvent = onEvent
                )
                is RecipeShimmeringUiModel -> RecipeBookmarkShimmeringItem()
                is RecipeProgressBarUiModel -> TokoNowLoadMoreProgressItem()
            }
        }
    }

    LaunchedEffect(scrollToTop) {
        coroutineScope.launch {
            if (scrollToTop) {
                lazyListState.animateScrollToItem(FIRST_ITEM_INDEX)
            }
        }
    }
}

@Composable
private fun RecipeBookmarkErrorLayout(
    error: RecipeBookmarkState.Error,
    onActionClick: (String?) -> Unit
) {
    TokoNowGlobalErrorLayout(
        throwable = error.throwable,
        errorCode = error.code,
        onActionClick = onActionClick
    )
}

fun LazyListState.isScrolledToEnd(): Boolean {
    val index = layoutInfo.visibleItemsInfo.lastOrNull()?.index
    val totalItems = layoutInfo.totalItemsCount - 1
    return index == totalItems
}

fun Visitable<*>.getId(index: Int): String {
    return when (this) {
        is RecipeUiModel -> this.getUniqueId()
        else -> index.toString() + this.hashCode()
    }
}

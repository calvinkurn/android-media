package com.tokopedia.common_compose.sort_filter

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.components.NestChips
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.R

@Composable
fun NestSortFilter(
    modifier: Modifier = Modifier,
    size: Size = Size.DEFAULT,
    items: List<SortFilter>,
    showClearFilterIcon: Boolean,
    onItemClicked: (SortFilter) -> Unit,
    onClearFilter: () -> Unit
) {
    val chipSize = when (size) {
        Size.DEFAULT -> com.tokopedia.common_compose.components.Size.SMALL
        Size.LARGE -> com.tokopedia.common_compose.components.Size.LARGE
    }
    Row(modifier = modifier) {
        val closeVisible by remember(items) {
            derivedStateOf { showClearFilterIcon && items.any { it.isSelected } }
        }
        AnimatedVisibility(closeVisible) {
            PrefixFilterItem(
                modifier = Modifier.padding(end = 4.dp),
                size = size,
                painterId = R.drawable.iconunify_close,
                onClick = onClearFilter
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items) {
                NestChips(
                    text = it.title,
                    isSelected = it.isSelected,
                    size = chipSize,
                    showChevron = it.showChevron,
                    onClick = {
                        onItemClicked(it)
                        it.onClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun LazyListState.horizontalOffset(): Int {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex == firstVisibleItemIndex && firstVisibleItemIndex == 0) {
                firstVisibleItemScrollOffset - previousScrollOffset
            } else {
                if (firstVisibleItemIndex > 0) {
                    0
                } else if (firstVisibleItemIndex < previousIndex) {
                    -1 * previousScrollOffset
                } else {
                    firstVisibleItemScrollOffset
                }
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun NestSortFilterAdvanced(
    modifier: Modifier = Modifier,
    size: Size = Size.DEFAULT,
    items: List<SortFilter>,
    onPrefixClicked: () -> Unit = {},
    onItemClicked: (SortFilter) -> Unit
) {
    val chipSize = when (size) {
        Size.DEFAULT -> com.tokopedia.common_compose.components.Size.SMALL
        Size.LARGE -> com.tokopedia.common_compose.components.Size.LARGE
    }
    val ld = LocalDensity.current
    val selectedSize = items.filter { it.isSelected }.size
    val rowState = rememberLazyListState()
    val offset = rowState.horizontalOffset()
    var currentTextWidth by remember { mutableStateOf<Dp?>(null) }
    var firstWidth by remember { mutableStateOf<Dp?>(null) }

    LaunchedEffect(key1 = offset, block = {
        if (currentTextWidth == null) return@LaunchedEffect
        val diffInDp = with(ld) { offset.toDp() }.times(1.7f)
        currentTextWidth = (currentTextWidth!! - diffInDp).coerceIn(0.dp, firstWidth)
    })

    val paddingEnd = if (currentTextWidth == 0.dp) 0.dp else 4.dp
    Row(modifier = modifier) {
        PrefixFilterItem(
            modifier = Modifier.padding(end = paddingEnd),
            size = size,
            text = "Filter",
            textWidth = currentTextWidth,
            textWidthChange = {
                currentTextWidth = with(ld) { it.toDp() }
                if (firstWidth == null) firstWidth = currentTextWidth
            },
            selectedSize = selectedSize,
            onClick = onPrefixClicked
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = rowState
        ) {
            items(items) {
                NestChips(
                    text = it.title,
                    isSelected = it.isSelected,
                    size = chipSize,
                    showChevron = it.showChevron,
                    onClick = {
                        onItemClicked(it)
                        it.onClick()
                    }
                )
            }
        }
    }
}

@Stable
data class SortFilter(
    val title: String,
    val isSelected: Boolean,
    val showChevron: Boolean = false,
    val onClick: () -> Unit = {}
)

enum class Size { DEFAULT, LARGE }

@Preview(name = "Sort Filter")
@Preview(name = "Sort Filter (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestSortFilterPreview() {
    var items by remember {
        mutableStateOf(
            listOf(
                SortFilter("Terrible", true, showChevron = true),
                SortFilter("Bad", false),
                SortFilter("Medium", false),
                SortFilter("Good", false),
                SortFilter("Impressive", false)
            )
        )
    }
    var advItems by remember {
        mutableStateOf(
            listOf(
                SortFilter("Micro", false),
                SortFilter("Tiny", false),
                SortFilter("Small", true),
                SortFilter("Medium", false),
                SortFilter("Big", false, showChevron = true),
                SortFilter("Enormous", false),
                SortFilter("Giant", false)
            )
        )
    }

    var size by remember { mutableStateOf(Size.DEFAULT) }
    NestTheme {
        Surface {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = size == Size.DEFAULT,
                        onCheckedChange = { size = Size.DEFAULT }
                    )
                    Text("S", fontWeight = FontWeight.Bold)
                    Checkbox(checked = size == Size.LARGE, onCheckedChange = { size = Size.LARGE })
                    Text("L", fontWeight = FontWeight.Bold)
                }
                Text(text = "Quick Filter")
                NestSortFilter(
                    size = size,
                    items = items,
                    showClearFilterIcon = true,
                    onItemClicked = { sf ->
                        items = items.map {
                            if (it == sf) {
                                it.copy(isSelected = it.isSelected.not())
                            } else {
                                it
                            }
                        }
                    },
                    onClearFilter = { items = items.map { it.copy(isSelected = false) } }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Advanced Filter")
                NestSortFilterAdvanced(items = advItems, size = size, onItemClicked = { sf ->
                    advItems = advItems.map {
                        if (it == sf) {
                            it.copy(isSelected = it.isSelected.not())
                        } else {
                            it
                        }
                    }
                })
            }
        }
    }
}

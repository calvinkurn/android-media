package com.tokopedia.common_compose.sort_filter

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.components.Color
import com.tokopedia.common_compose.components.NestChips
import com.tokopedia.common_compose.components.NestNotification
import com.tokopedia.common_compose.ui.LocalNestColor
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
            derivedStateOf {  showClearFilterIcon && items.any { it.isSelected } }
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

data class SortFilter(
    val title: String,
    val isSelected: Boolean,
    val showChevron: Boolean = false,
    val onClick: () -> Unit
)

enum class Size { DEFAULT, LARGE }

@Composable
private fun PrefixFilterItem(
    modifier: Modifier = Modifier,
    size: Size = Size.DEFAULT,
    selectedSize: Int = 0,
    painterId: Int = R.drawable.iconunify_sort_filter,
    text: String? = null,
    onClick: () -> Unit = {}
) {
    // Implementation are specifically to cater SELECTED and NORMAL type chips only
    val backgroundColor = NestTheme.colors.NN._0
    val borderColor = NestTheme.colors.NN._200
    val height = when (size) {
        Size.DEFAULT -> 32.dp
        Size.LARGE -> 48.dp
    }
    Surface(color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .height(height)
            .clickable { onClick() }) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedSize > 0) {
                NestNotification(text = selectedSize.toString(), colorType = Color.SECONDARY)
            } else {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = painterId),
                    contentDescription = "Clear Filter Icon",
                    tint = LocalNestColor.current.NN._500
                )
            }
            if (text != null) {
                Text(modifier = Modifier.padding(start = 4.dp), text = text)
            }
        }
    }
}

@Preview(name = "Prefix Sortfilter")
@Preview(name = "Prefix Sortfilter (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PrefixSortFilterPreview() {
    NestTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            PrefixFilterItem(text = "Filter")
            PrefixFilterItem(text = "Filter", selectedSize = 3)
            PrefixFilterItem(painterId = R.drawable.iconunify_close)
        }
    }
}

@Preview(name = "Sort Filter")
@Preview(name = "Sort Filter (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestSortFilterPreview() {
    var items by remember {
        mutableStateOf(
            listOf(
                SortFilter("Terrible", true, showChevron = true, onClick = {}),
                SortFilter("Bad", false, onClick = {}),
                SortFilter("Medium", false, onClick = {}),
                SortFilter("Good", false, onClick = {}),
                SortFilter("Impressive", false, onClick = {}),
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
                        onCheckedChange = { size = Size.DEFAULT })
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
                            if (it == sf) it.copy(isSelected = it.isSelected.not())
                            else it
                        }
                    },
                    onClearFilter = { items = items.map { it.copy(isSelected = false) } })
            }
        }
    }
}

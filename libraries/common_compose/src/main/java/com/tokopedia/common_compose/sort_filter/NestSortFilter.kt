package com.tokopedia.common_compose.sort_filter

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import com.tokopedia.common_compose.components.NestChips
import com.tokopedia.common_compose.components.Size
import com.tokopedia.common_compose.ui.LocalNestColor
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.R

data class SortFilter(
    val title: String,
    val isSelected: Boolean,
    val showChevron: Boolean = false,
    val onClick: () -> Unit
)

@Composable
fun NestSortFilter(
    modifier: Modifier = Modifier,
    size: Size = Size.SMALL,
    items: ArrayList<SortFilter>,
    showClearFilterIcon: Boolean,
    onClearFilter: () -> Unit
) {
    // Implementation are specifically to cater filterRelationship = SortFilter.RELATIONSHIP_AND filterType = SortFilter.TYPE_QUICK only
    Row {
        if (showClearFilterIcon) PrefixFilterItem(
            modifier = Modifier.padding(end = 12.dp),
            size = size,
            painterId = R.drawable.iconunify_close,
            onClick = onClearFilter
        )
        LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items) {
                NestChips(
                    text = it.title,
                    isSelected = it.isSelected,
                    size = size,
                    showChevron = it.showChevron,
                    onClick = it.onClick
                )
            }
        }
    }
}

@Composable
private fun PrefixFilterItem(
    modifier: Modifier = Modifier,
    size: Size = Size.SMALL,
    painterId: Int = R.drawable.iconunify_sort_filter,
    text: String? = null,
    onClick: () -> Unit = {}
) {
    // Implementation are specifically to cater SELECTED and NORMAL type chips only
    val backgroundColor = NestTheme.colors.NN._0
    val borderColor = NestTheme.colors.NN._200
    val height = when (size) {
        Size.SMALL -> 32.dp
        Size.MEDIUM -> 40.dp
        Size.LARGE -> 48.dp
    }
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .height(height)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = painterId),
                contentDescription = "Clear Filter Icon",
                tint = LocalNestColor.current.NN._500
            )
            if (text != null) {
                Text(modifier = Modifier.padding(start = 4.dp), text = text)
            }
        }
    }
}

@Preview(name = "Prefix Sortfilter")
@Composable
fun PrefixSortFilterPreview() {
    NestTheme {
        Row {
            PrefixFilterItem(text = "Filter")
            PrefixFilterItem(painterId = R.drawable.iconunify_close)
        }
    }
}

@Preview(name = "Sort Filter")
@Preview(name = "Sort Filter (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestSortFilterPreview() {
    val items = arrayListOf(
        SortFilter("Lokasi", true, showChevron = true, onClick = {}),
        SortFilter("Status", false, onClick = {})
    )
    var size by remember { mutableStateOf(Size.SMALL) }
    NestTheme {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = size == Size.SMALL, onCheckedChange = { size = Size.SMALL })
                Text("S", fontWeight = FontWeight.Bold)
                Checkbox(checked = size == Size.MEDIUM, onCheckedChange = { size = Size.MEDIUM })
                Text("M", fontWeight = FontWeight.Bold)
                Checkbox(checked = size == Size.LARGE, onCheckedChange = { size = Size.LARGE })
                Text("L", fontWeight = FontWeight.Bold)
            }
            NestSortFilter(
                modifier = Modifier,
                size = size,
                items = items,
                showClearFilterIcon = true,
                onClearFilter = {}
            )
        }
    }
}

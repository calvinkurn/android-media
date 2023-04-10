package com.tokopedia.common_compose.sort_filter

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.components.NestChip
import com.tokopedia.common_compose.components.Size
import com.tokopedia.common_compose.ui.LocalNestColor
import com.tokopedia.common_compose.ui.NestTheme

data class SortFilter(val title: String, val isSelected: Boolean, val onClick: () -> Unit)

@Composable
fun NestSortFilter(
    modifier: Modifier = Modifier,
    items: ArrayList<SortFilter>,
    onClearFilter: () -> Unit,
    showClearFilterIcon: Boolean
) {
    // Implementation are specifically to cater filterRelationship = SortFilter.RELATIONSHIP_AND filterType = SortFilter.TYPE_QUICK only
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        if (showClearFilterIcon) item { ClearSortFilterItem(onClick = onClearFilter) }
        items(items) {
            NestChip(
                text = it.title,
                isSelected = it.isSelected,
                size = Size.SMALL,
                showChevron = true,
                onClick = it.onClick
            )
        }
    }
}

@Composable
private fun ClearSortFilterItem(
    iconVector: ImageVector = Icons.Outlined.Close,
    text: String? = null,
    onClick: () -> Unit = {}
) {
    // Implementation are specifically to cater SELECTED and NORMAL type chips only
    val backgroundColor = NestTheme.colors.NN._0
    val borderColor = NestTheme.colors.NN._200
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .height(32.dp)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = iconVector,
                contentDescription = "Clear Filter Icon",
                tint = LocalNestColor.current.NN._500
            )
            if (text != null) {
                Text(text = text)
            }
        }
    }
}

@Preview(name = "Prefix Sortfilter")
@Composable
fun PrefixSortFilterPreview() {
    NestTheme {
        ClearSortFilterItem(iconVector = Icons.Default.Settings, text = "Filter")
    }
}

@Preview(name = "Sort Filter")
@Preview(name = "Sort Filter (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestSortFilterPreview() {
    val items = arrayListOf(
        SortFilter("Lokasi", true, onClick = {}),
        SortFilter("Status", false, onClick = {})
    )
    NestTheme {
        NestSortFilter(
            modifier = Modifier,
            items = items,
            onClearFilter = {},
            showClearFilterIcon = true
        )
    }
}

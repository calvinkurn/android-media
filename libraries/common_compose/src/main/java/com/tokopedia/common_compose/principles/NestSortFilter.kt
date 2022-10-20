package com.tokopedia.common_compose.principles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.R
import com.tokopedia.common_compose.ui.NestTheme

data class SortFilter(val title: String, val isSelected : Boolean, val onClick: () -> Unit)

@Composable
fun NestSortFilter(
    modifier: Modifier = Modifier,
    items: ArrayList<SortFilter>,
    onClearFilter: () -> Unit,
    showClearFilterIcon: Boolean
) {
    //Implementation are specifically to cater filterRelationship = SortFilter.RELATIONSHIP_AND filterType = SortFilter.TYPE_QUICK only
    LazyRow(modifier = modifier) {
        if (showClearFilterIcon) item { ClearSortFilterItem(onClearFilter) }
        items(items) { NestSortFilterItem(it) }
    }
}

@Composable
private fun ClearSortFilterItem(onClearFilter: () -> Unit) {
    //Implementation are specifically to cater SELECTED and NORMAL type chips only
    val backgroundColor= NestTheme.colors.NN0
    val borderColor = NestTheme.colors.NN200
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .height(32.dp)
            .padding(end = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Close,
            modifier = Modifier
                .clickable { onClearFilter() }
                .padding(horizontal = 12.dp),
            contentDescription = "Clear Filter Icon",
            tint = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN500)
        )
    }
}


@Preview(name = "Sort Filter Item (Selected)")
@Composable
fun NestSortFilterItemSelectedPreview() {
    NestSortFilterItem(SortFilter("Lokasi", true, {}))
}

@Preview(name = "Sort Filter Item (Default)")
@Composable
fun NestSortFilterItemPreview() {
    NestSortFilterItem(SortFilter("Lokasi", false, {}))
}


@Composable
private fun NestSortFilterItem(sortFilter: SortFilter) {
    //Implementation are specifically to cater SELECTED and NORMAL type chips only
    val textColorSelected = NestTheme.colors.GN500
    val textColorDefault = NestTheme.colors.NN600

    val borderColorSelected = NestTheme.colors.GN400
    val borderColorDefault = NestTheme.colors.NN300

    val backgroundColorSelected = NestTheme.colors.GN50
    val backgroundColorDefault = NestTheme.colors.NN0

    val textColor = if (sortFilter.isSelected) {
        textColorSelected
    } else {
        textColorDefault
    }

    val borderColor = if (sortFilter.isSelected) {
        borderColorSelected
    } else {
        borderColorDefault
    }

    val backgroundColor = if (sortFilter.isSelected) {
        backgroundColorSelected
    } else {
        backgroundColorDefault
    }

    val chevronColor = if (sortFilter.isSelected) {
        NestTheme.colors.GN500
    } else {
        NestTheme.colors.NN900
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .height(32.dp)
            .padding(end = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { sortFilter.onClick() }
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NestTypography(text = sortFilter.title, textStyle = NestTheme.typography.display2.copy(color = textColor))
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_down),
                contentDescription = "Dropdown Icon",
                tint = chevronColor
            )
        }
    }
}


@Preview(name = "Sort Filter")
@Composable
fun NestSortFilterPreview() {
    val items = arrayListOf(
        SortFilter("Lokasi", true, onClick = {}),
        SortFilter("Status", false, onClick = {})
    )
    NestSortFilter(modifier = Modifier, items = items, onClearFilter = {}, showClearFilterIcon = true)
}


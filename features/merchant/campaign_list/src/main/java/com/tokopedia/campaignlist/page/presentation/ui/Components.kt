package com.tokopedia.campaignlist.page.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.campaignlist.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify

data class SortFilter(val title: String, val isSelected : Boolean, val onClick: () -> Unit)

@Composable
fun UnifySortFilter(
    modifier: Modifier = Modifier,
    items: ArrayList<SortFilter>,
    onClearFilter: () -> Unit
) {
    //Implementation are specifically to cater filterRelationship = SortFilter.RELATIONSHIP_AND filterType = SortFilter.TYPE_QUICK,
    LazyRow(modifier = modifier) {
        item { ClearSortFilterItem(onClearFilter) }
        items(items) { UnifySortFilterItem(it) }
    }
}

@Composable
fun ClearSortFilterItem(onClearFilter: () -> Unit) {
    val backgroundColor= colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN0)
    val borderColor = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN200)
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

@Composable
private fun UnifySortFilterItem(sortFilter: SortFilter) {
    val textColorSelected = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_GN500)
    val textColorDefault = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN600)

    val textColor = if (sortFilter.isSelected) {
        textColorSelected
    } else {
        textColorDefault
    }

    val borderColorSelected = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_GN400)
    val borderDefault = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN300)

    val borderColor = if (sortFilter.isSelected) {
        borderColorSelected
    } else {
        borderDefault
    }

    val backgroundColorSelected = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_GN50)
    val backgroundColorDefault = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN0)

    val backgroundColor = if (sortFilter.isSelected) {
        backgroundColorSelected
    } else {
        backgroundColorDefault
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
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = sortFilter.title, color = textColor)
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_down),
                contentDescription = "Dropdown Icon"
            )
        }
    }
}

@Composable
fun UnifySearchBar(
    modifier: Modifier = Modifier,
    placeholderText: String,
    onTextChanged: (String) -> Unit = { _ -> },
    onSearchBarCleared: () -> Unit = {},
    onKeyboardSearchAction: (String) -> Unit
) {
    val borderColor = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN200)
    val searchIconColor = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN500)

    var text by remember { mutableStateOf("") }

    BasicTextField(
        modifier = modifier
            .height(36.dp)
            .border(0.5.dp, borderColor, RoundedCornerShape(8.dp))
        ,
        value = text,
        onValueChange = { newText ->
            onTextChanged(newText)
            text = newText
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { onKeyboardSearchAction(text) }),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Search Icon",
                    tint = searchIconColor
                )

                Spacer(modifier = Modifier.width(6.dp))

                Box(Modifier.weight(1f)) {

                    if (text.isEmpty()) {
                        UnifyTypography(
                            text = placeholderText,
                            weight = UnifyTypographyWeight.REGULAR,
                            type = UnifyTypographyType.DISPLAY_2,
                            colorId = com.tokopedia.unifyprinciples.R.color.Unify_NN600
                        )
                    }


                    innerTextField()
                }

                if (text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                text = ""
                                onSearchBarCleared()
                            },
                        contentDescription = "Close Icon",
                        tint = searchIconColor
                    )
                }
            }
        }
    )
}

@Composable
fun UnifyTicker(
    modifier: Modifier = Modifier,
    text: CharSequence,
    onDismissed: () -> Unit = {}
) {
    val iconColor = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_BN400)
    val strokeColor = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_BN200)
    val backgroundColor = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_BN50)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, strokeColor)
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Icon(imageVector = Icons.Outlined.Info, contentDescription = "Information Icon", tint = iconColor)
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            UnifyTypography(
                text = text.toString(),
                modifier = Modifier.width(250.dp),
                weight = UnifyTypographyWeight.REGULAR,
                type = UnifyTypographyType.DISPLAY_3,
                colorId = com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Icon(
                imageVector = Icons.Outlined.Close,
                modifier = Modifier.clickable { onDismissed() },
                contentDescription = "Close Icon"
            )
        }
    }

}


@Composable
fun UnifyLabel(
    modifier: Modifier = Modifier,
    labelText: CharSequence,
    unifyLabelType: UnifyLabelType
) {
    val backgroundColor = colorResource(id = unifyLabelType.backgroundColorResourceId)
    val textColor = colorResource(id = unifyLabelType.textColorResourceId)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = backgroundColor
    ) {
        Text(
            text = labelText.toString(),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
    
}

@Composable
fun UnifyButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {

    val backgroundColor = ButtonDefaults.buttonColors(
        backgroundColor = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_GN500)
    )

    Button(
        modifier = modifier.height(32.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = backgroundColor
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun UnifyTypography(
    text: String,
    modifier: Modifier = Modifier,
    type: UnifyTypographyType,
    weight: UnifyTypographyWeight = UnifyTypographyWeight.REGULAR,
    colorId : Int
) {
    val fontSize = type.fontSize.sp
    val letterSpacing = type.letterSpacing.sp
    val fontWeight = if (weight == UnifyTypographyWeight.BOLD) FontWeight.Bold else FontWeight.Normal
    val textColor = colorResource(id = colorId)

    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        letterSpacing = letterSpacing,
        fontWeight = fontWeight,
        color = textColor
    )
}

@Composable
fun UnifyImage(modifier: Modifier = Modifier, imageUrl : String) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ImageUnify(context)
        },
        update = {
            it.loadImage(imageUrl)
        }
    )
}

@Preview(name = "Sort Filter")
@Composable
fun UnifySortFilterPreview() {
    val items = arrayListOf(
        SortFilter("Lokasi", true, onClick = {}),
        SortFilter("Status", false, onClick = {})
    )
    UnifySortFilter(modifier = Modifier, items = items, onClearFilter = {})
}

@Preview(name = "Sort Filter Item (Selected)")
@Composable
fun UnifySortFilterItemSelectedPreview() {
    UnifySortFilterItem(SortFilter("Lokasi", true, {}))
}

@Preview(name = "Sort Filter Item (Default)")
@Composable
fun UnifySortFilterItemPreview() {
    UnifySortFilterItem(SortFilter("Lokasi", false, {}))
}
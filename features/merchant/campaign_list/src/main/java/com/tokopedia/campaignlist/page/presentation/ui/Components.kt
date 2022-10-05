package com.tokopedia.campaignlist.page.presentation.ui

import android.view.KeyEvent
import android.widget.TextView
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.campaignlist.common.util.onTextChanged
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography

@Composable
fun UnifySortFilter(
    modifier: Modifier = Modifier,
    items: ArrayList<SortFilterItem>,
    filterRelationship: Int,
    filterType: Int,
    onClearFilter: () -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            SortFilter(context).apply {
                dismissListener = onClearFilter
            }
        },
        update = {
            it.filterRelationship = filterRelationship
            it.filterType = filterType
            it.addItem(items)
        }
    )
}

@Composable
fun UnifySearchBar(
    modifier: Modifier = Modifier,
    placeholderText: String,
    onTextChanged: (String) -> Unit = { _ -> },
    onSearchBarCleared: () -> Unit = {},
    onEditorAction: (TextView?, Int?, KeyEvent?) -> Boolean
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            SearchBarUnify(context).apply {
                searchBarTextField.onTextChanged(onTextChanged)
                searchBarTextField.setOnEditorActionListener(onEditorAction)
            }
        },
        update = {
            it.showIcon = false
            it.searchBarPlaceholder = placeholderText
            it.clearListener = onSearchBarCleared
        }
    )
}

@Composable
fun UnifyTicker(
    modifier: Modifier = Modifier,
    text: CharSequence,
    tickerShape: Int,
    tickerType: Int,
    onHyperlinkClicked: (CharSequence) -> Unit = {},
    onDismissed: () -> Unit = {}
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            Ticker(context).apply {
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        onHyperlinkClicked(linkUrl)
                    }

                    override fun onDismiss() {
                        onDismissed()
                    }
                })
            }
        },
        update = {
            it.setTextDescription(text)
            it.tickerShape = tickerShape
            it.tickerType = tickerType
        }
    )
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
    type: Int,
    weight: Int = Typography.REGULAR,
    colorId: Int
) {
    Text(text = text, modifier = modifier)
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
package com.tokopedia.campaignlist.page.presentation.fragment

import android.content.res.ColorStateList
import android.view.KeyEvent
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.tokopedia.campaignlist.common.util.onTextChanged
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.UnifyButton
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
                addItem(items)
                this.filterRelationship = filterRelationship
                this.filterType = filterType
                dismissListener = onClearFilter
            }
        },
        update = {
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
                showIcon = false
                searchBarPlaceholder = placeholderText
                clearListener = onSearchBarCleared
            }
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
                setTextDescription(text)
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        onHyperlinkClicked(linkUrl)
                    }

                    override fun onDismiss() {
                        onDismissed()
                    }

                })
                this.tickerShape = tickerShape
                this.tickerType = tickerType
            }
        }
    )
}


@Composable
fun UnifyLabel(
    modifier: Modifier = Modifier,
    labelText: CharSequence,
    labelType: Int
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            Label(context).apply {
                setLabelType(labelType)
                setLabel(labelText.toString())
            }
        }
    )
}

@Composable
fun UnifyButton(
    modifier: Modifier = Modifier,
    text: String,
    buttonSize: Int,
    buttonVariant : Int,
    buttonType : Int,
    onClick: () -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            UnifyButton(context).apply {
                this.text = text
                this.buttonSize = buttonSize
                this.buttonVariant = buttonVariant
                this.buttonType = buttonType
                setOnClickListener { onClick() }
            }
        }
    )
}

@Composable
fun UnifyTypography(
    text: String,
    modifier: Modifier = Modifier,
    type: Int,
    weight: Int = Typography.REGULAR,
    colorId: Int
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val stateList = arrayOf(intArrayOf(android.R.attr.state_enabled))
            val color = intArrayOf(ContextCompat.getColor(context, colorId))
            val colorStateList = ColorStateList(stateList, color)

            Typography(context).apply {
                this.text = text
                setType(type)
                setWeight(weight)
                setTextColor(colorStateList)
            }
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun RemoteImage(modifier: Modifier = Modifier, imageUrl : String) {
    val painter = rememberImagePainter(data = imageUrl)
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}
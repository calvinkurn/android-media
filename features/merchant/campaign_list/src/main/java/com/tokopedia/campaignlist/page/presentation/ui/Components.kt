package com.tokopedia.campaignlist.page.presentation.ui

import android.content.res.ColorStateList
import android.view.KeyEvent
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import com.tokopedia.campaignlist.common.util.onTextChanged
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ImageUnify
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
    labelType: Int
) {
    AndroidView(
        modifier = modifier,
        factory = { context -> Label(context) },
        update = {
            it.setLabelType(labelType)
            it.setLabel(labelText.toString())
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
            UnifyButton(context)
        },
        update = {
            it.text = text
            it.buttonSize = buttonSize
            it.buttonVariant = buttonVariant
            it.buttonType = buttonType
            it.setOnClickListener { onClick() }
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
            Typography(context)
        },
        update = {
            val stateList = arrayOf(intArrayOf(android.R.attr.state_enabled))
            val color = intArrayOf(ContextCompat.getColor(it.context, colorId))
            val colorStateList = ColorStateList(stateList, color)
            it.setType(type)
            it.setWeight(weight)
            it.setTextColor(colorStateList)
            it.text = text
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
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
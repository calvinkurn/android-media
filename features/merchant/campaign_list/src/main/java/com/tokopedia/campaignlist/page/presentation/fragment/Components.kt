package com.tokopedia.campaignlist.page.presentation.fragment

import android.content.res.ColorStateList
import android.view.KeyEvent
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
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
    onDismissed: () -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->

            SortFilter(context).apply {
                addItem(items)
                filterRelationship = SortFilter.RELATIONSHIP_AND
                filterType = SortFilter.TYPE_QUICK
                dismissListener = onDismissed
            }
        },
        update = { view ->

        }
    )
}

@Composable
fun UnifySearchBar(
    modifier: Modifier = Modifier,
    placeholderText: String,
    onTextChanged: (String) -> Unit,
    onEditorAction: (TextView, Int, KeyEvent) -> Boolean
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            SearchBarUnify(context).apply {
                searchBarTextField.onTextChanged { value ->
                    onTextChanged(value)
                }
                searchBarTextField.setOnEditorActionListener { textView, actionId, event ->
                    onEditorAction(textView, actionId, event)
                }
                showIcon = false
                searchBarPlaceholder = placeholderText
            }
        },
        update = { view ->

        }
    )
}

@Composable
fun UnifyTicker(
    modifier: Modifier = Modifier,
    text: CharSequence,
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
                tickerShape = Ticker.SHAPE_LOOSE
                tickerType = Ticker.TYPE_ANNOUNCEMENT
            }
        },
        update = { view ->

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
                text = labelText
            }
        },
        update = { view ->

        }
    )
}

@Composable
fun ComposeButton(
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
        },
        update = { view ->

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
        },
        update = { view ->

        }
    )
}
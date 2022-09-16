package com.tokopedia.campaignlist.page.presentation.fragment

import android.view.KeyEvent
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.campaignlist.common.util.onTextChanged
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

@Composable
fun ComposeSortFilter(
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
fun ComposeSearchBar(
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
fun ComposeTicker(
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
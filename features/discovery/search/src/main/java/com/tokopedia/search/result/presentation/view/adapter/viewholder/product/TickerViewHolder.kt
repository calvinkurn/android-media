package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductTickerLayoutBinding
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.presentation.view.listener.TickerListener
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding

class TickerViewHolder(
        itemView: View,
        private val tickerListener: TickerListener
) : AbstractViewHolder<TickerDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_ticker_layout
    }

    private var binding : SearchResultProductTickerLayoutBinding? by viewBinding()

    override fun bind(element: TickerDataView) {
        bindTickerView(element)
    }

    private fun bindTickerView(element: TickerDataView) {
        val shouldShowTicker = !(tickerListener.isTickerHasDismissed || element.text.isEmpty())

        val tickerView = binding?.root ?: return

        tickerView.shouldShowWithAction(shouldShowTicker) {
            tickerView.addOnImpressionListener(element) {
                tickerListener.onTickerImpressed(element)
            }
            tickerView.setHtmlDescription(element.text)
            tickerView.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    onDescriptionViewClick(element)
                }

                override fun onDismiss() {
                    onTickerDismissed()
                }
            })
        }
    }

    private fun onDescriptionViewClick(element: TickerDataView) {
        if (element.query.isNotEmpty()) {
            tickerListener.onTickerClicked(element)
        }
    }

    private fun onTickerDismissed() {
        binding?.root?.gone()
        tickerListener.onTickerDismissed()
    }
}
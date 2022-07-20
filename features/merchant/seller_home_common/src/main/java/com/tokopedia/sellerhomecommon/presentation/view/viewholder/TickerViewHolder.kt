package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcTickerWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerWidgetUiModel
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback

/**
 * Created By @ilhamsuaib on 10/08/20
 */

class TickerViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<TickerWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_ticker_widget
    }

    private val binding by lazy { ShcTickerWidgetBinding.bind(itemView) }

    override fun bind(element: TickerWidgetUiModel) {
        val tickerData = element.data
        with(binding.tickerViewShc) {
            val tickers = tickerData?.tickers.orEmpty().map {
                TickerData(it.title, it.message, it.type, true, it)
            }
            val tickerAdapter = TickerPagerAdapter(context, tickers)
            addPagerView(tickerAdapter, tickers)

            tickerAdapter.onDismissListener = {
                listener.removeWidget(adapterPosition, element)
            }

            tickerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {

                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    val item = itemData as? TickerItemUiModel
                    if (!RouteManager.route(context, linkUrl.toString())) {
                        item?.let {
                            RouteManager.route(context, it.redirectUrl)
                        }
                    }
                    item?.let {
                        listener.sendTickerCtaClickEvent(it)
                    }
                }
            })
            rootView.addOnImpressionListener(element.impressHolder) {
                listener.sendTickerImpression(tickerData?.tickers.orEmpty())
            }
        }

        hideTickerIfEmpty(element)
    }

    private fun hideTickerIfEmpty(element: TickerWidgetUiModel) {
        val isEmpty = element.data?.tickers.isNullOrEmpty()
        val height0dp = 0

        val tickerHeight = if (isEmpty) {
            height0dp
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT
        }
        itemView.layoutParams.height = tickerHeight
        itemView.requestLayout()
    }

    interface Listener : BaseViewHolderListener {
        fun sendTickerImpression(tickers: List<TickerItemUiModel>) {}
        fun sendTickerCtaClickEvent(ticker: TickerItemUiModel) {}
    }
}
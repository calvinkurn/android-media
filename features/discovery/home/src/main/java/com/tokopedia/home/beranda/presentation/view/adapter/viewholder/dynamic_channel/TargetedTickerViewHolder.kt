package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.databinding.LayoutTargetedTickerBinding
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR

class TargetedTickerViewHolder(
    view: View,
    private val listener: HomeCategoryListener
) : AbstractViewHolder<TickerDataModel>(view) {

    private val binding: LayoutTargetedTickerBinding? by viewBinding()

    private var tickerId = ""

    override fun bind(element: TickerDataModel?) {
        if (element == null) return

        setupTickerAdapter(element)
        requestFocusOnTicker()
    }

    @SuppressLint("DeprecatedMethod")
    private fun setupTickerAdapter(element: TickerDataModel) {
        val adapter = TickerPagerAdapter(itemView.context, element.unifyTickers)
        binding?.component?.addPagerView(adapter, element.unifyTickers)

        // set pager description
        adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
            override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                listener.onSectionItemClicked(linkUrl.toString())

                val index = findTickerPosition(element.unifyTickers, itemData)
                if (index != -1) {
                    tickerId = element.targetedTickers[index].id.orZero().toString()
                    HomePageTracking.eventClickTickerHomePage(tickerId)
                }
            }
        })

        // set description event
        adapter.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) = Unit

            override fun onDismiss() {
                listener.onCloseTicker()
                HomePageTracking.eventClickOnCloseTickerHomePage(tickerId)
            }
        })
    }

    private fun findTickerPosition(tickers: List<TickerData>, itemData: Any?): Int {
        val ticker = itemData as? TickerData ?: return -1
        return tickers.indexOf(ticker)
    }

    private fun requestFocusOnTicker() {
        binding?.component?.postDelayed({
            try {
                itemView.findViewById<View>(unifycomponentsR.id.ticker_content_multiple).requestLayout()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, FOCUS_DELAYED_INTERVAL)
    }

    companion object {
        private const val FOCUS_DELAYED_INTERVAL = 1000L

        @LayoutRes
        val LAYOUT = R.layout.layout_targeted_ticker
    }
}

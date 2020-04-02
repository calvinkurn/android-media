package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerViewModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ANNOUNCEMENT
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import java.util.*

/**
 * @author by DevAra on 02/04/20.
 */
class TickerViewHolder(itemView: View, private val listener: HomeCategoryListener) : AbstractViewHolder<TickerViewModel?>(itemView) {
    private val emptyTitle: String = ""
    private val tickerComponent: Ticker = itemView.findViewById(R.id.tickerComponent)
    private val context: Context = itemView.context
    private val view: View = itemView
    private val tickerId = ""

    override fun bind(element: TickerViewModel?) {
        element?.let {element->
            element.tickers?.let {tickers->
                val tickerDataList: MutableList<TickerData> = ArrayList()

                for (tickerData in tickers) {
                    tickerDataList.add(TickerData(emptyTitle, tickerData.message.toString(), TYPE_ANNOUNCEMENT, true))
                }
                val tickerPagerAdapter = TickerPagerAdapter(context, tickerDataList)
                tickerComponent.addPagerView(tickerPagerAdapter, tickerDataList)
                tickerComponent.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        HomePageTracking.eventClickTickerHomePage(context,tickerId)
                        listener.onSectionItemClicked(linkUrl.toString())
                    }

                    override fun onDismiss() {
                        HomePageTracking.eventClickOnCloseTickerHomePage(context,tickerId);
                        listener.onCloseTicker();
                    }
                })
            }
        }
    }

    companion object {
        private val TAG = TickerViewHolder::class.java.simpleName

        @LayoutRes
        val LAYOUT = R.layout.layout_ticker_home
    }

}
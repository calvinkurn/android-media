package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.unifycomponents.ticker.*
import java.util.*

/**
 * @author by DevAra on 02/04/20.
 */
class TickerViewHolder(itemView: View, private val listener: HomeCategoryListener) : AbstractViewHolder<TickerDataModel?>(itemView) {
    private val tickerComponent: Ticker = itemView.findViewById(R.id.tickerComponent)
    private val context: Context = itemView.context
    private val view: View = itemView
    private val tickerId = ""

    override fun bind(element: TickerDataModel?) {
        element?.let {element->
            element.tickers.let {tickers->
                val tickerDataList: MutableList<TickerData> = ArrayList()

                for (tickerData in tickers) {
                    tickerDataList.add(TickerData(tickerData.title, tickerData.message, tickerData.tickerType, true))
                }
                val tickerPagerAdapter = TickerPagerAdapter(context, tickerDataList)
                tickerComponent.addPagerView(tickerPagerAdapter, tickerDataList)
                tickerPagerAdapter.setPagerDescriptionClickEvent(object: TickerPagerCallback {
                    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                        HomePageTracking.eventClickTickerHomePage(tickerId)
                        listener.onSectionItemClicked(linkUrl.toString())
                    }

                })
                tickerComponent.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {

                    }

                    override fun onDismiss() {
                        HomePageTracking.eventClickOnCloseTickerHomePage(tickerId);
                        listener.onCloseTicker();
                    }
                })

                tickerComponent.postDelayed({
                    try {
                        view.findViewById<View>(R.id.ticker_content_multiple).requestLayout()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, 1000)
            }
        }
    }

    companion object {
        private val TAG = TickerViewHolder::class.java.simpleName

        @LayoutRes
        val LAYOUT = R.layout.layout_ticker_home
    }

}
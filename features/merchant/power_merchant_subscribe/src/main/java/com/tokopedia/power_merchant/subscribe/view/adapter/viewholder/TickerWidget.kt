package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.data.source.local.model.TickerUiModel
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.WidgetTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import kotlinx.android.synthetic.main.widget_pm_ticker.view.*

/**
 * Created By @ilhamsuaib on 30/03/21
 */

class TickerWidget(
        itemView: View,
        private val listener: Listener
) : AbstractViewHolder<WidgetTickerUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_ticker
    }

    override fun bind(element: WidgetTickerUiModel) {
        with(itemView.tickerPmWidget) {
            val tickersData = element.tickers.map { ticker ->
                val tickerType: Int = when (ticker.type) {
                    TickerUiModel.TYPE_ERROR -> Ticker.TYPE_ERROR
                    TickerUiModel.TYPE_WARNING -> Ticker.TYPE_WARNING
                    else -> Ticker.TYPE_ANNOUNCEMENT
                }
                return@map TickerData(ticker.title, ticker.text, tickerType, true, ticker)
            }
            val adapter = TickerPagerAdapter(context, tickersData)
            addPagerView(adapter, tickersData)
            adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    RouteManager.route(context, linkUrl.toString())
                }
            })
            adapter.onDismissListener = {
                removeTicker()
            }
        }
    }

    private fun removeTicker() {
        listener.setOnTickerWidgetRemoved(adapterPosition)
    }

    interface Listener {
        fun setOnTickerWidgetRemoved(position: Int)
    }
}
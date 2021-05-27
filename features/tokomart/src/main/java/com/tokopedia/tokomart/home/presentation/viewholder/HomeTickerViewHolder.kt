package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback

class HomeTickerViewHolder(
        itemView: View
) : AbstractViewHolder<HomeTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokonow_home_ticker
    }

    private var ticker: Ticker? = null

    init {
        ticker = itemView.findViewById(R.id.ticker_announcement)
    }

    override fun bind(data: HomeTickerUiModel) {
        val dataList = mapTickerData(data.tickers.tickerList)
        val adapter = TickerPagerAdapter(itemView.context, dataList)
        adapter.apply {
            setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    Toast.makeText(itemView.context, linkUrl, Toast.LENGTH_SHORT).show()
                }
            })
            onDismissListener = {
                Toast.makeText(itemView.context, "Ticker Slider Dismissed", Toast.LENGTH_SHORT).show()
            }
        }
        ticker?.addPagerView(adapter, dataList)
    }

    private fun mapTickerData(tickerList: List<com.tokopedia.tokomart.home.domain.model.Ticker> ): List<TickerData> {
        val uiTickerList = ArrayList<TickerData>()
        tickerList.map { tickerData ->
            uiTickerList.add(
                    TickerData(
                            title = tickerData.title,
                            description = tickerData.message,
                            type = Ticker.TYPE_ANNOUNCEMENT
                    )
            )
        }
        return uiTickerList
    }
}
package com.tokopedia.logisticCommon.util

import android.content.Context
import android.view.View
import com.tokopedia.logisticCommon.domain.model.TickerModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback

object TargetedTickerHelper {
    fun TickerModel.renderView(
        context: Context,
        ticker: Ticker,
        onClickApplink: (String) -> Unit,
        onClickUrl: (String) -> Unit
    ) {
        if (this.item.isNotEmpty()) {
            val message = ArrayList<TickerData>()
            for (tickerItem in item) {
                message.add(
                    TickerData(
                        tickerItem.title,
                        tickerItem.content,
                        tickerItem.type,
                        true,
                        tickerItem.linkUrl
                    )
                )
            }
            val tickerPageAdapter = TickerPagerAdapter(context, message)
            tickerPageAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    val link = linkUrl.toString()
                    if (link.startsWith("tokopedia")) {
                        onClickApplink(link)
                    } else {
                        onClickUrl(link)
                    }
                }
            })
            ticker.addPagerView(tickerPageAdapter, message)
            ticker.visibility = View.VISIBLE
        } else {
            ticker.visibility = View.GONE
        }
    }
}

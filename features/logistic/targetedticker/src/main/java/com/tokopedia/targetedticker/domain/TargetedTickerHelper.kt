package com.tokopedia.targetedticker.domain

import android.content.Context
import android.view.View
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback

object TargetedTickerHelper {
    fun Ticker.renderTargetedTickerView(
        context: Context,
        model: TickerModel,
        onClickApplink: (String) -> Unit,
        onClickUrl: (String) -> Unit
    ) {
        if (model.item.isNotEmpty()) {
            val message = model.toTickerData()
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
            this.addPagerView(tickerPageAdapter, message)
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

    private fun TickerModel.toTickerData(): ArrayList<TickerData> {
        val message = ArrayList<TickerData>()
        for (tickerItem in this.item) {
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
        return message
    }
}

package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback

class HomeTickerViewHolder(
        itemView: View
) : AbstractViewHolder<HomeTickerUiModel>(itemView), TickerPagerCallback {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokonow_home_ticker
        const val PREFIX_LINK = "tokopedia"
    }

    override fun bind(data: HomeTickerUiModel) {
        val ticker: Ticker = itemView.findViewById(R.id.ticker_announcement)
        val adapter = TickerPagerAdapter(itemView.context, data.tickers)
        adapter.setPagerDescriptionClickEvent(this)
        ticker.post {
            ticker.addPagerView(adapter, data.tickers)
        }
    }

    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
        val context = itemView.context
        val url = linkUrl.toString()
        if (url.startsWith(PREFIX_LINK)) {
            RouteManager.route(context, url)
        } else {
            RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${url}")
        }
    }
}
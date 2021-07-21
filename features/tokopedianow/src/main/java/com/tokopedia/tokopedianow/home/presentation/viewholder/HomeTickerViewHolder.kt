package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageButton
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback

class HomeTickerViewHolder(
        itemView: View,
        private val listener: HomeTickerListener? = null
) : AbstractViewHolder<HomeTickerUiModel>(itemView), TickerPagerCallback {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_ticker
        const val PREFIX_LINK = "tokopedia"
    }

    override fun bind(data: HomeTickerUiModel) {
        if(data.tickers.isNotEmpty()) {
            val closeBtn = itemView.findViewById<AppCompatImageButton>(com.tokopedia.unifycomponents.R.id.ticker_close_icon)
            val ticker = itemView.findViewById<Ticker>(R.id.ticker_announcement)
            closeBtn.setOnClickListener {
                listener?.onTickerDismissed(data.id)
            }
            val adapter = TickerPagerAdapter(itemView.context, data.tickers)
            adapter.setPagerDescriptionClickEvent(this)
            ticker.addPagerView(adapter, data.tickers)
            itemView.show()
        } else {
            itemView.hide()
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

    interface HomeTickerListener {
        fun onTickerDismissed(id: String)
    }
}
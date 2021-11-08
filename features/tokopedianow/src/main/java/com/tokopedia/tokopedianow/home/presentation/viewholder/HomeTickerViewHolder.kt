package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeTickerBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeTickerUiModel
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.utils.view.binding.viewBinding

class HomeTickerViewHolder(
        itemView: View,
        private val listener: HomeTickerListener? = null
) : AbstractViewHolder<HomeTickerUiModel>(itemView), TickerPagerCallback {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_ticker
        const val PREFIX_LINK = "tokopedia"
    }

    private var binding: ItemTokopedianowHomeTickerBinding? by viewBinding()

    override fun bind(data: HomeTickerUiModel) {
        binding?.tickerAnnouncement?.post {
            val adapter = TickerPagerAdapter(itemView.context, data.tickers)
            adapter.setPagerDescriptionClickEvent(this)
            adapter.onDismissListener = {
                listener?.onTickerDismissed(data.id)
            }
            binding?.tickerAnnouncement?.addPagerView(adapter, data.tickers)
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
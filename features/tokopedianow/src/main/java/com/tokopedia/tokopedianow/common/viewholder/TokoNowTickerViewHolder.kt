package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeTickerBinding
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowTickerViewHolder(
        itemView: View
) : AbstractViewHolder<TokoNowTickerUiModel>(itemView), TickerPagerCallback {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_ticker
        const val PREFIX_LINK = "tokopedia"
    }

    private var binding: ItemTokopedianowHomeTickerBinding? by viewBinding()

    override fun bind(data: TokoNowTickerUiModel) {
        val adapter = TickerPagerAdapter(itemView.context, data.tickers)
        adapter.setPagerDescriptionClickEvent(this)
        binding?.ticker?.addPagerView(adapter, data.tickers)
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

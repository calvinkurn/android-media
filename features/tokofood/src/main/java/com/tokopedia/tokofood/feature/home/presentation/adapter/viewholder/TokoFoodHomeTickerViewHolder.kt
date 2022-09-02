package com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.ItemTokofoodTickerBinding
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.utils.view.binding.viewBinding

class TokoFoodHomeTickerViewHolder(
    itemView: View,
    private val listener: TokoFoodHomeTickerListener? = null
) : AbstractViewHolder<TokoFoodHomeTickerUiModel>(itemView), TickerPagerCallback {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_ticker
        const val PREFIX_LINK = "tokopedia://"
    }

    private var binding: ItemTokofoodTickerBinding? by viewBinding()
    private var ticker: Ticker? = null

    override fun bind(data: TokoFoodHomeTickerUiModel) {
        setupLayout()
        setTickerData(data)
    }

    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
        val context = itemView.context
        val url = linkUrl.toString()
        if (url.startsWith(PREFIX_LINK)) {
            TokofoodRouteManager.routePrioritizeInternal(context, url)
        } else {
            RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${url}")
        }
    }

    private fun setupLayout() {
        ticker = binding?.tickerHomeTokofood
    }

    private fun setTickerData(data: TokoFoodHomeTickerUiModel) {
        val adapter = TickerPagerAdapter(itemView.context, data.tickers)
        adapter.setPagerDescriptionClickEvent(this)
        adapter.onDismissListener = {
            listener?.onTickerDismissed(data.id)
        }
        ticker?.addPagerView(adapter, data.tickers)
    }

    interface TokoFoodHomeTickerListener {
        fun onTickerDismissed(id: String)
    }
}
package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeTickerBinding
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil
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
        binding?.apply {
            addPagerView(
                adapter = adapter,
                data = data
            )
            setBackgroundColor(
                data = data
            )
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

    private fun ItemTokopedianowHomeTickerBinding.addPagerView(
        adapter: TickerPagerAdapter,
        data: TokoNowTickerUiModel
    ) {
        ticker.addPagerView(
            adapter = adapter,
            adapterData = data.tickers
        )
    }

    private fun ItemTokopedianowHomeTickerBinding.setBackgroundColor(
        data: TokoNowTickerUiModel
    ) {
        if (data.backgroundColor.isNotBlank()) {
            root.setBackgroundColor(
                ViewUtil.safeParseColor(
                    color = data.backgroundColor,
                    defaultColor = ContextCompat.getColor(
                        itemView.context,
                        R.color.tokopedianow_card_dms_color
                    )
                )
            )
        }
    }
}

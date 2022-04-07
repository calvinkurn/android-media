package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingTickerBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.TickerInfoData
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.utils.view.binding.viewBinding

class OrderTrackingTickerViewHolder(
    itemView: View,
    private val orderTrackingListener: OrderTrackingListener
) : AbstractViewHolder<TickerInfoData>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_ticker
    }

    private val binding: ItemTokofoodOrderTrackingTickerBinding? by viewBinding()

    override fun bind(element: TickerInfoData) {
        binding?.run {
            if (element.tickerList.isNotEmpty()) {
                setupOrderTrackingTicker(element.tickerList)
            } else {
                tickerOrderDetail.hide()
            }
        }
    }

    private fun ItemTokofoodOrderTrackingTickerBinding.setupOrderTrackingTicker(tickerList: List<TickerData>) {
        tickerOrderDetail.run {
            val tickerPagerAdapter = TickerPagerAdapter(context, tickerList)
            addPagerView(tickerPagerAdapter, tickerList)
            tickerPagerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    if (linkUrl.isNotEmpty()) {
                        orderTrackingListener.onTickerLinkClick(linkUrl.toString())
                    }
                }
            })
        }
    }

    interface Listener {
        fun onTickerLinkClick(linkUrl: String)
    }
}
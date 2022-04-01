package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingTickerBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.utils.view.binding.viewBinding

class OrderTrackingTickerViewHolder(itemView: View): AbstractViewHolder<OrderTrackingTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_ticker

        const val TICKER_INFO_TYPE = "info"
        const val TICKER_ANNOUNCE_TYPE = "announce"
    }

    private val binding: ItemTokofoodOrderTrackingTickerBinding? by viewBinding()

    override fun bind(element: OrderTrackingTickerUiModel) {
        binding?.run {
            tickerOrderTrackingLive.apply {
                tickerType = mapTickerType(element.type)
            }
        }
    }

    private fun mapTickerType(type: String): Int {
        return when (type) {
            TICKER_INFO_TYPE -> Ticker.TYPE_INFORMATION
            TICKER_ANNOUNCE_TYPE -> Ticker.TYPE_ANNOUNCEMENT
            else -> 0
        }
    }
}
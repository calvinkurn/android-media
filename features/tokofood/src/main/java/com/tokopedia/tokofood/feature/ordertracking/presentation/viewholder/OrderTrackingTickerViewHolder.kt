package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingTickerBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingTickerUiModel
import com.tokopedia.utils.view.binding.viewBinding

class OrderTrackingTickerViewHolder(itemView: View): AbstractViewHolder<OrderTrackingTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_ticker
    }

    private val binding: ItemTokofoodOrderTrackingTickerBinding? by viewBinding()

    override fun bind(element: OrderTrackingTickerUiModel?) {
        binding?.run {
            tickerOrderTrackingLive.apply {

            }
        }
    }

}
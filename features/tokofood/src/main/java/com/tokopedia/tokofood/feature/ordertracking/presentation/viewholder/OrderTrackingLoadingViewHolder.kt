package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingLoadingUiModel

class OrderTrackingLoadingViewHolder(itemView: View): AbstractViewHolder<OrderTrackingLoadingUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_order_tracking_shimmer
    }

    override fun bind(element: OrderTrackingLoadingUiModel?) {}

}
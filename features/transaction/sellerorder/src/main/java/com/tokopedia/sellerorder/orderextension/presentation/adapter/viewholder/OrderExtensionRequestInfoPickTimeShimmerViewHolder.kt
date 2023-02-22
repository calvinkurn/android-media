package com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel

class OrderExtensionRequestInfoPickTimeShimmerViewHolder(
    itemView: View?
) : AbstractViewHolder<OrderExtensionRequestInfoUiModel.PickTimeShimmerUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_order_extension_request_info_shimmer_pick_time
    }

    override fun bind(element: OrderExtensionRequestInfoUiModel.PickTimeShimmerUiModel?) {}
}

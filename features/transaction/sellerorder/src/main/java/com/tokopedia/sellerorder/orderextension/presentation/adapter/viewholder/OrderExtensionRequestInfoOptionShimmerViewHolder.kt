package com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel

class OrderExtensionRequestInfoOptionShimmerViewHolder(
    itemView: View?
) : AbstractViewHolder<OrderExtensionRequestInfoUiModel.OptionShimmerUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_order_extension_request_info_shimmer_option
    }

    override fun bind(element: OrderExtensionRequestInfoUiModel.OptionShimmerUiModel?) {}
}
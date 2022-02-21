package com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemOrderExtensionRequestInfoShimmerDescriptionBinding
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.utils.view.binding.viewBinding

class OrderExtensionRequestInfoDescriptionShimmerViewHolder(
    itemView: View?
) : AbstractViewHolder<OrderExtensionRequestInfoUiModel.DescriptionShimmerUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_order_extension_request_info_shimmer_description
    }

    private val binding by viewBinding<ItemOrderExtensionRequestInfoShimmerDescriptionBinding>()

    override fun bind(element: OrderExtensionRequestInfoUiModel.DescriptionShimmerUiModel?) {
        binding?.root?.run {
            val layoutParamsCopy = layoutParams
            layoutParamsCopy.width = element?.width?.getDimen(context)?.toInt() ?: layoutParamsCopy.width
            layoutParams = layoutParamsCopy
        }
    }
}
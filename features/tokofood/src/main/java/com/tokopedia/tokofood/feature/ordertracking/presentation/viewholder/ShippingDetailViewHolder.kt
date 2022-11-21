package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderDetailShippingSectionBinding
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ShippingDetailUiModel

class ShippingDetailViewHolder(view: View):
    CustomPayloadViewHolder<ShippingDetailUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_detail_shipping_section
    }

    private val binding = ItemTokofoodOrderDetailShippingSectionBinding.bind(itemView)

    override fun bind(element: ShippingDetailUiModel) {
        with(binding) {
            setMerchantName(element.merchantName)
            setFullDestination(element.getFullDestination())
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let {
            val (oldItem, newItem) = it
            if (oldItem is ShippingDetailUiModel && newItem is ShippingDetailUiModel) {
                if (oldItem.merchantName != newItem.merchantName) {
                    binding.setMerchantName(newItem.merchantName)
                }
                if (isNotEqualsDestination(oldItem, newItem)) {
                    binding.setFullDestination(newItem.getFullDestination())
                }
            }
        }
    }

    private fun ItemTokofoodOrderDetailShippingSectionBinding.setMerchantName(merchantName: String) {
        tvOrderDetailRestoValue.text = merchantName
    }

    private fun ItemTokofoodOrderDetailShippingSectionBinding.setFullDestination(fullDestination: String) {
        tvOrderDetailDestionationValue.text = MethodChecker.fromHtmlWithoutExtraSpace(fullDestination)
    }

    private fun isNotEqualsDestination(oldItem: ShippingDetailUiModel, newItem: ShippingDetailUiModel): Boolean {
        return oldItem.destinationName != newItem.destinationName ||
                oldItem.destinationPhone != newItem.destinationPhone ||
                oldItem.destinationAddress != newItem.destinationAddress
    }

}

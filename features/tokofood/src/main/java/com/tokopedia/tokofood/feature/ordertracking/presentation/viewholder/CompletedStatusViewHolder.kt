package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderDetailStatusInfoHeaderBinding
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.CompletedStatusInfoUiModel

class CompletedStatusViewHolder(view: View): CustomPayloadViewHolder<CompletedStatusInfoUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_detail_status_info_header
    }

    private val binding = ItemTokofoodOrderDetailStatusInfoHeaderBinding.bind(itemView)

    override fun bind(element: CompletedStatusInfoUiModel) {
        with(binding) {
            setOrderStatus(element.orderStatus)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let {
            val (oldItem, newItem) = it
            if (oldItem is CompletedStatusInfoUiModel && newItem is CompletedStatusInfoUiModel) {
                if (oldItem.orderStatus != newItem.orderStatus) {
                    binding.setOrderStatus(newItem.orderStatus)
                }
            }
        }
    }

    private fun ItemTokofoodOrderDetailStatusInfoHeaderBinding.setOrderStatus(orderStatus: String) {
        tvOrderDetailStatusOrder.text = orderStatus
    }
}
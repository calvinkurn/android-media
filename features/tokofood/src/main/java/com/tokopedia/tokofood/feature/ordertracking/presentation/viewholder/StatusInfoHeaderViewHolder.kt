package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderDetailStatusInfoHeaderBinding
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingPaymentGrandTotalBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.StatusInfoHeaderUiModel

class StatusInfoHeaderViewHolder(view: View): BaseOrderTrackingViewHolder<StatusInfoHeaderUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_detail_status_info_header
    }

    private val binding = ItemTokofoodOrderDetailStatusInfoHeaderBinding.bind(itemView)

    override fun bind(element: StatusInfoHeaderUiModel) {
        with(binding) {
            setOrderStatus(element.orderStatus)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let {
            val (oldItem, newItem) = it
            if (oldItem is StatusInfoHeaderUiModel && newItem is StatusInfoHeaderUiModel) {
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
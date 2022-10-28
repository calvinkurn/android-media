package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderDetailPaymentInfoBinding
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentDetailUiModel

class PaymentDetailViewHolder(view: View) : CustomPayloadViewHolder<PaymentDetailUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_detail_payment_info
    }

    private val binding = ItemTokofoodOrderDetailPaymentInfoBinding.bind(itemView)

    override fun bind(element: PaymentDetailUiModel) {
        with(binding) {
            setPaymentInfoLabel(element.paymentInfoLabel)
            setPaymentInfoValue(element.paymentInfoValue)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let {
            val (oldItem, newItem) = it
            if (oldItem is PaymentDetailUiModel && newItem is PaymentDetailUiModel) {
                if (oldItem.paymentInfoLabel != newItem.paymentInfoLabel) {
                    binding.setPaymentInfoLabel(newItem.paymentInfoLabel)
                }
                if (oldItem.paymentInfoValue != newItem.paymentInfoValue) {
                    binding.setPaymentInfoLabel(newItem.paymentInfoValue)
                }
            }
        }
    }

    private fun ItemTokofoodOrderDetailPaymentInfoBinding.setPaymentInfoLabel(paymentInfoLabel: String) {
        tvOrderTrackingPaymentInfoLabel.text = paymentInfoLabel
    }

    private fun ItemTokofoodOrderDetailPaymentInfoBinding.setPaymentInfoValue(paymentInfoValue: String) {
        tvOrderTrackingInfoValue.text = paymentInfoValue
    }
}
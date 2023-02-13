package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingPaymentGrandTotalBinding
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentAmountUiModel

class PaymentAmountViewHolder(view: View) :
    CustomPayloadViewHolder<PaymentAmountUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_payment_grand_total
    }

    private val binding = ItemTokofoodOrderTrackingPaymentGrandTotalBinding.bind(itemView)

    override fun bind(element: PaymentAmountUiModel) {
        with(binding) {
            setGrandTotalLabel(element.totalLabel)
            setGrandTotalValue(element.grandTotalValue)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let {
            val (oldItem, newItem) = it
            if (oldItem is PaymentAmountUiModel && newItem is PaymentAmountUiModel) {
                if (oldItem.totalLabel != newItem.totalLabel) {
                    binding.setGrandTotalLabel(newItem.totalLabel)
                }
                if (oldItem.grandTotalValue != newItem.grandTotalValue) {
                    binding.setGrandTotalValue(newItem.grandTotalValue)
                }
            }
        }
    }

    private fun ItemTokofoodOrderTrackingPaymentGrandTotalBinding.setGrandTotalLabel(grandTotalLabel: String) {
        tvOrderTrackingGrandTotalLabel.text = grandTotalLabel
    }

    private fun ItemTokofoodOrderTrackingPaymentGrandTotalBinding.setGrandTotalValue(grandTotalValue: String) {
        tvOrderTrackingGrandTotalValue.text = grandTotalValue
    }
}
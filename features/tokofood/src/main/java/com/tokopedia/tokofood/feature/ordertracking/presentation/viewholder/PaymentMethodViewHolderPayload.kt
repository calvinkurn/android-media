package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingPaymentMethodBinding
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentMethodUiModel

class PaymentMethodViewHolderPayload(view: View) :
    CustomPayloadViewHolder<PaymentMethodUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_payment_method
    }

    private val binding = ItemTokofoodOrderTrackingPaymentMethodBinding.bind(itemView)

    override fun bind(element: PaymentMethodUiModel) {
        with(binding) {
            setMethodLabel(element.methodLabel)
            setMethodValue(element.methodValue)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let {
            val (oldItem, newItem) = it
            if (oldItem is PaymentMethodUiModel && newItem is PaymentMethodUiModel) {
                if (oldItem.methodLabel != newItem.methodLabel) {
                    binding.setMethodLabel(newItem.methodLabel)
                }
                if (oldItem.methodValue != newItem.methodValue) {
                    binding.setMethodValue(newItem.methodValue)
                }
            }
        }
    }

    private fun ItemTokofoodOrderTrackingPaymentMethodBinding.setMethodLabel(methodLabel: String) {
        tvOrderTrackingPaymentMethodLabel.text = methodLabel
    }

    private fun ItemTokofoodOrderTrackingPaymentMethodBinding.setMethodValue(methodValue: String) {
        tvOrderTrackingPaymentMethodValue.text = methodValue
    }
}
package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_payment_grand_total.view.*

class PaymentGrandTotalViewHolder(itemView: View?) : AbstractViewHolder<PaymentInfoUiModel.PaymentGrandTotalUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_payment_grand_total
    }

    override fun bind(element: PaymentInfoUiModel.PaymentGrandTotalUiModel?) {
        element?.let {
            setupPaymentLabel(it.label)
            setupPaymentValue(it.value)
        }
    }

    override fun bind(element: PaymentInfoUiModel.PaymentGrandTotalUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val oldItem = it.first
                val newItem = it.second
                if (oldItem is PaymentInfoUiModel.PaymentGrandTotalUiModel && newItem is PaymentInfoUiModel.PaymentGrandTotalUiModel) {
                    itemView.container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.label != newItem.label) {
                        setupPaymentLabel(newItem.label)
                    }
                    if (oldItem.value != newItem.value) {
                        setupPaymentValue(newItem.value)
                    }
                    itemView.container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupPaymentLabel(label: String) {
        itemView.tvBuyerOrderDetailPaymentInfoLabel?.text = label
    }

    private fun setupPaymentValue(value: String) {
        itemView.tvBuyerOrderDetailPaymentInfoValue?.text = value
    }
}
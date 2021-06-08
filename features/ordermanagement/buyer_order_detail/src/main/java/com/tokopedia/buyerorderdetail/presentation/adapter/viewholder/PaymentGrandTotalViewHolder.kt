package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import com.tokopedia.unifyprinciples.Typography

class PaymentGrandTotalViewHolder(itemView: View?) : AbstractViewHolder<PaymentInfoUiModel.PaymentGrandTotalUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_payment_grand_total
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val tvBuyerOrderDetailPaymentInfoLabel = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailPaymentInfoLabel)
    private val tvBuyerOrderDetailPaymentInfoValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailPaymentInfoValue)

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
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.label != newItem.label) {
                        setupPaymentLabel(newItem.label)
                    }
                    if (oldItem.value != newItem.value) {
                        setupPaymentValue(newItem.value)
                    }
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupPaymentLabel(label: String) {
        tvBuyerOrderDetailPaymentInfoLabel?.text = label
    }

    private fun setupPaymentValue(value: String) {
        tvBuyerOrderDetailPaymentInfoValue?.text = value
    }
}
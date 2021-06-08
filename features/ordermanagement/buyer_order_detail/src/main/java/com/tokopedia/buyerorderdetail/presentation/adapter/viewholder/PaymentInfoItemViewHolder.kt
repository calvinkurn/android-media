package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import com.tokopedia.unifyprinciples.Typography

class PaymentInfoItemViewHolder(itemView: View?): AbstractViewHolder<PaymentInfoUiModel.PaymentInfoItemUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_payment_info_item
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val tvBuyerOrderDetailPaymentInfoLabel = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailPaymentInfoLabel)
    private val tvBuyerOrderDetailPaymentInfoValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailPaymentInfoValue)

    override fun bind(element: PaymentInfoUiModel.PaymentInfoItemUiModel?) {
        element?.let {
            setupPaymentInfoLabel(it.label)
            setupPaymentInfoValue(it.value)
        }
    }

    override fun bind(element: PaymentInfoUiModel.PaymentInfoItemUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val oldItem = it.first
                val newItem = it.second
                if (oldItem is PaymentInfoUiModel.PaymentInfoItemUiModel && newItem is PaymentInfoUiModel.PaymentInfoItemUiModel) {
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.label != newItem.label) {
                        setupPaymentInfoLabel(newItem.label)
                    }
                    if (oldItem.value != newItem.value) {
                        setupPaymentInfoValue(newItem.value)
                    }
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupPaymentInfoLabel(label: String) {
        tvBuyerOrderDetailPaymentInfoLabel?.text = label
    }

    private fun setupPaymentInfoValue(value: String) {
        tvBuyerOrderDetailPaymentInfoValue?.text = value
    }
}
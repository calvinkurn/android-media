package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_payment_info_item.view.*

class PaymentInfoItemViewHolder(itemView: View?): AbstractViewHolder<PaymentInfoUiModel.PaymentInfoItemUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_payment_info_item
    }

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
                    itemView.container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.label != newItem.label) {
                        setupPaymentInfoLabel(newItem.label)
                    }
                    if (oldItem.value != newItem.value) {
                        setupPaymentInfoValue(newItem.value)
                    }
                    itemView.container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupPaymentInfoLabel(label: String) {
        itemView.tvBuyerOrderDetailPaymentInfoLabel?.text = label
    }

    private fun setupPaymentInfoValue(value: String) {
        itemView.tvBuyerOrderDetailPaymentInfoValue?.text = value
    }
}
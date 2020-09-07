package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrder
import kotlinx.android.synthetic.main.item_waiting_payment_order.view.*

class WaitingPaymentOrderProductViewHolder(itemView: View?) : AbstractViewHolder<WaitingPaymentOrder.Product>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_order
    }

    @SuppressLint("SetTextI18n")
    override fun bind(element: WaitingPaymentOrder.Product?) {
        element?.let { element ->
            with(itemView) {
                ivProduct.urlSrc = element.picture
                tvProductName.text = element.name
                tvProductQuantityAndPrice.text = "${element.quantity} x ${element.price}"
            }
        }
    }
}
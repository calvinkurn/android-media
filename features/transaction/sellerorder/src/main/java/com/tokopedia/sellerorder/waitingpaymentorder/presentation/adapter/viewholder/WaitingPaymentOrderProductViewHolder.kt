package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import kotlinx.android.synthetic.main.item_waiting_payment_order.view.*

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderProductViewHolder(
        itemView: View?
) : AbstractViewHolder<WaitingPaymentOrderUiModel.ProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_order
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderUiModel.ProductUiModel?) {
        element?.let { element ->
            with(itemView) {
                ivProduct.urlSrc = element.picture
                tvProductName.text = element.name
                tvProductQuantityAndPrice.text = context.getString(
                        R.string.waiting_payment_orders_product_quantity_and_price,
                        element.quantity,
                        element.price)
            }
        }
    }
}
package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemWaitingPaymentOrderBinding
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderProductViewHolder(
        itemView: View
) : AbstractViewHolder<WaitingPaymentOrderUiModel.ProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_order
    }

    private val binding by viewBinding<ItemWaitingPaymentOrderBinding>()

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderUiModel.ProductUiModel?) {
        element?.let { element ->
            binding?.run {
                ivProduct.urlSrc = element.picture
                tvProductName.text = element.name
                tvProductQuantityAndPrice.text = root.context.getString(
                        R.string.waiting_payment_orders_product_quantity_and_price,
                        element.quantity,
                        element.price)
            }
        }
    }
}
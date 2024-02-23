package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutPaymentBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPaymentModel
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidget

class CheckoutPaymentViewHolder(private val binding: ItemCheckoutPaymentBinding,
                                private val listener: CheckoutAdapterListener) : RecyclerView.ViewHolder(binding.root) {

    fun bind(payment: CheckoutPaymentModel) {
        binding.root.setContent {
            CheckoutPaymentWidget(
                payment.widget,
                onRetryClickedListener = { listener.onRetryGetPayment() }
            )
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_payment
    }
}

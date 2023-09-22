package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutButtonPaymentBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel

class CheckoutButtonPaymentViewHolder(private val binding: ItemCheckoutButtonPaymentBinding, private val listener: CheckoutAdapterListener) : RecyclerView.ViewHolder(binding.root) {

    fun bind(buttonPayment: CheckoutButtonPaymentModel) {
        CheckoutButtonPaymentItemView.renderButtonPayment(buttonPayment, binding, listener)
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_button_payment
    }
}

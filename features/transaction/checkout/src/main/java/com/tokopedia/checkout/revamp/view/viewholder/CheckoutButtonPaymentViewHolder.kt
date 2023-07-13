package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutButtonPaymentBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel

class CheckoutButtonPaymentViewHolder(private val binding: ItemCheckoutButtonPaymentBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(buttonPayment: CheckoutButtonPaymentModel) {
    }

    companion object {

        val VIEW_TYPE = R.layout.item_checkout_button_payment
    }
}

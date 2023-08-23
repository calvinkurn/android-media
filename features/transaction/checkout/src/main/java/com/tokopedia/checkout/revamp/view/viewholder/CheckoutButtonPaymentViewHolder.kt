package com.tokopedia.checkout.revamp.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutButtonPaymentBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel

class CheckoutButtonPaymentViewHolder(private val binding: ItemCheckoutButtonPaymentBinding, private val listener: CheckoutAdapterListener) : RecyclerView.ViewHolder(binding.root) {

    fun bind(buttonPayment: CheckoutButtonPaymentModel) {
        binding.root.visibility = View.INVISIBLE
        CheckoutButtonPaymentItemView.renderButtonPayment(buttonPayment, binding, listener)
        listener.onBindButtonPayment()
    }

    fun hide() {
        binding.root.visibility = View.INVISIBLE
    }

    fun show() {
        binding.root.visibility = View.VISIBLE
    }

    companion object {

        val VIEW_TYPE = R.layout.item_checkout_button_payment
    }
}

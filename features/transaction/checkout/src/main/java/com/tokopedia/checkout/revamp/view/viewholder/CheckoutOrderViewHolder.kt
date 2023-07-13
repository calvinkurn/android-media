package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutOrderBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel

class CheckoutOrderViewHolder(private val binding: ItemCheckoutOrderBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(order: CheckoutOrderModel) {
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_order
    }
}

package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutAddressBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel

class CheckoutAddressViewHolder(private val binding: ItemCheckoutAddressBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(address: CheckoutAddressModel) {
        binding.tvCheckoutAddressTitle.text = address.recipientAddressModel.addressName
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_address
    }
}

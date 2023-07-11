package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentProductBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel

class CheckoutProductViewHolder(private val binding: ItemShipmentProductBinding, private val actionListener: CheckoutAdapterListener): RecyclerView.ViewHolder(binding.root) {

    fun bind(product: CheckoutProductModel) {
        binding.ivProductImage.setImageUrl(product.imageUrl)
        binding.tvProductName.text = product.name
    }

    companion object {
        val VIEW_TYPE = R.layout.item_shipment_product
    }
}

package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofProductStaticBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductStaticUiModel

class PofProductStaticViewHolder(view: View) : AbstractViewHolder<PofProductStaticUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_product_static
    }

    private val binding = ItemPofProductStaticBinding.bind(view)

    override fun bind(element: PofProductStaticUiModel) {
        setupProductImage(element.productImageUrl)
        setupProductName(element.productName)
        setupProductPriceQuantity(element.productPriceQuantity)
        setupProductQuantity(element.productQuantity)
    }

    override fun bind(element: PofProductStaticUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupProductImage(productImageUrl: String) {
        binding.ivPofProductImage.loadImage(productImageUrl)
    }

    private fun setupProductName(productName: String) {
        binding.tvPofProductName.text = productName
    }

    private fun setupProductPriceQuantity(productPriceQuantity: String) {
        binding.tvPofProductPriceQuantity.text = productPriceQuantity
    }

    private fun setupProductQuantity(productQuantity: Int) {
        binding.tvPofProductQuantity.text = productQuantity.toString()
    }
}

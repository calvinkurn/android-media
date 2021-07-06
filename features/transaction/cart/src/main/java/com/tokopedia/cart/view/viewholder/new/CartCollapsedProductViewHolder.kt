package com.tokopedia.cart.view.viewholder.new

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartCollapsedProductBinding
import com.tokopedia.cart.view.uimodel.new.CartCollapsedProductHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class CartCollapsedProductViewHolder(val viewBinding: ItemCartCollapsedProductBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        var LAYOUT = R.layout.item_cart_collapsed_product
    }

    fun bind(cartCollapsedProductHolderData: CartCollapsedProductHolderData) {
        renderProductImage(cartCollapsedProductHolderData)
        renderVariant(cartCollapsedProductHolderData)
        renderQuantity(cartCollapsedProductHolderData)
    }

    private fun renderProductImage(cartCollapsedProductHolderData: CartCollapsedProductHolderData) {
        ImageHandler.loadImageWithoutPlaceholder(viewBinding.imageProduct, cartCollapsedProductHolderData.productImageUrl)
    }

    private fun renderQuantity(cartCollapsedProductHolderData: CartCollapsedProductHolderData) {
        val textQuantity = "${cartCollapsedProductHolderData.productQuantity} barang"
        viewBinding.textProductQuantity.text = textQuantity
    }

    private fun renderVariant(cartCollapsedProductHolderData: CartCollapsedProductHolderData) {
        if (cartCollapsedProductHolderData.productVariantName.isNotBlank()) {
            viewBinding.textVariantName.text = cartCollapsedProductHolderData.productVariantName
            viewBinding.textVariantName.show()
        } else {
            viewBinding.textVariantName.gone()
        }
    }

}
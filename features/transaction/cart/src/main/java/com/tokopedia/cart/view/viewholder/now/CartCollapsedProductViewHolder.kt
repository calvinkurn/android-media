package com.tokopedia.cart.view.viewholder.now

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartCollapsedProductBinding
import com.tokopedia.cart.view.uimodel.now.CartCollapsedProductHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.currency.CurrencyFormatUtil

class CartCollapsedProductViewHolder(val viewBinding: ItemCartCollapsedProductBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        var LAYOUT = R.layout.item_cart_collapsed_product
    }

    fun bind(cartCollapsedProductHolderData: CartCollapsedProductHolderData) {
        renderImage(cartCollapsedProductHolderData)
        renderVariant(cartCollapsedProductHolderData)
        renderPrice(cartCollapsedProductHolderData)
        renderQuantity(cartCollapsedProductHolderData)
    }

    private fun renderPrice(cartCollapsedProductHolderData: CartCollapsedProductHolderData) {
        val productPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartCollapsedProductHolderData.productPrice, false)
        viewBinding.textProductPrice.text = productPrice
    }

    private fun renderImage(cartCollapsedProductHolderData: CartCollapsedProductHolderData) {
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
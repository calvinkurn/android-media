package com.tokopedia.cart.view.viewholder.now

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartCollapsedProductBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.currency.CurrencyFormatUtil

class CartCollapsedProductViewHolder(val viewBinding: ItemCartCollapsedProductBinding, val actionListener: ActionListener, val parentPosition: Int) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        var LAYOUT = R.layout.item_cart_collapsed_product
    }

    fun bind(cartItemHolderData: CartItemHolderData) {
        renderImage(cartItemHolderData)
        renderVariant(cartItemHolderData)
        renderPrice(cartItemHolderData)
        renderQuantity(cartItemHolderData)
    }

    private fun renderPrice(cartItemHolderData: CartItemHolderData) {
        val productPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartItemHolderData.cartItemData.originData.pricePlanInt, false)
        viewBinding.textProductPrice.text = productPrice
    }

    private fun renderImage(cartItemHolderData: CartItemHolderData) {
        ImageHandler.loadImageWithoutPlaceholder(viewBinding.imageProduct, cartItemHolderData.cartItemData.originData.productImage)
        viewBinding.imageProduct.setOnClickListener {
            if (parentPosition != RecyclerView.NO_POSITION) {
                actionListener.onExpandAvailableItem(parentPosition)
            }
        }
    }

    private fun renderQuantity(cartItemHolderData: CartItemHolderData) {
        val textQuantity = "${cartItemHolderData.cartItemData.updatedData.quantity} barang"
        viewBinding.textProductQuantity.text = textQuantity
    }

    private fun renderVariant(cartItemHolderData: CartItemHolderData) {
        if (cartItemHolderData.cartItemData.originData.variant.isNotBlank()) {
            viewBinding.textVariantName.text = cartItemHolderData.cartItemData.originData.variant
            viewBinding.textVariantName.show()
        } else {
            viewBinding.textVariantName.gone()
        }
    }

}
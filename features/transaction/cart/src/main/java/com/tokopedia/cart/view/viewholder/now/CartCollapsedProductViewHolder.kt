package com.tokopedia.cart.view.viewholder.now

import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartCollapsedProductBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.currency.CurrencyFormatUtil


class CartCollapsedProductViewHolder(val viewBinding: ItemCartCollapsedProductBinding, val actionListener: ActionListener) : RecyclerView.ViewHolder(viewBinding.root) {

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
        val productPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(cartItemHolderData.productPrice, false)
        viewBinding.textProductPrice.text = productPrice
    }

    private fun renderImage(cartItemHolderData: CartItemHolderData) {
        ImageHandler.loadImageWithoutPlaceholder(viewBinding.imageProduct, cartItemHolderData.productImage)
        viewBinding.imageProduct.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                actionListener.onCollapsedProductClicked(position, cartItemHolderData)
            }
        }
    }

    private fun renderQuantity(cartItemHolderData: CartItemHolderData) {
        val textQuantity = "${cartItemHolderData.quantity} barang"
        viewBinding.textProductQuantity.text = textQuantity
    }

    private fun renderVariant(cartItemHolderData: CartItemHolderData) {
        if (cartItemHolderData.variant.isNotBlank()) {
            viewBinding.textVariantName.text = cartItemHolderData.variant
            viewBinding.textVariantName.show()
        } else {
            viewBinding.textVariantName.gone()
        }

        setPriceConstraint()
    }

    private fun setPriceConstraint() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(viewBinding.containerCollapsedProduct)
        if (viewBinding.textVariantName.isVisible) {
            val margin = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_2)
            constraintSet.connect(R.id.text_product_price, ConstraintSet.TOP, R.id.text_variant_name, ConstraintSet.BOTTOM, margin)
        } else {
            val margin = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_4)
            constraintSet.connect(R.id.text_product_price, ConstraintSet.TOP, R.id.text_variant_name, ConstraintSet.BOTTOM, margin)
        }
        constraintSet.applyTo(viewBinding.containerCollapsedProduct)
    }

}
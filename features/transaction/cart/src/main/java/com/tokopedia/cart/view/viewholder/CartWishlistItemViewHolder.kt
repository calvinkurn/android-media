package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemProductWishlistBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartWishlistItemViewHolder(private val binding: ItemProductWishlistBinding, val actionListener: ActionListener?, val itemWidth: Int) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_product_wishlist
    }

    fun bind(element: CartWishlistItemHolderData) {
        itemView.setOnClickListener {
            actionListener?.onWishlistProductClicked(element.id)
        }

        renderProductImage(element)
        renderProductName(element)
        renderProductVariant(element)
        renderProductPrice(element)
        renderShopBadge(element)
        renderShopName(element)
        renderFreeShipping(element)
        renderButtonDelete(element)
        renderButtonAddToCart(element)
    }

    private fun renderButtonAddToCart(element: CartWishlistItemHolderData) {
        binding.btnAddToCart.apply {
            setOnClickListener {
                actionListener?.onButtonAddToCartClicked(element)
            }
        }
    }

    private fun renderButtonDelete(element: CartWishlistItemHolderData) {
        binding.btnDeleteWishlist.apply {
            setOnClickListener {
                actionListener?.onRemoveWishlistFromWishlist(element.id)
            }
        }
    }

    private fun renderFreeShipping(element: CartWishlistItemHolderData) {
        binding.imgFreeShipping.apply {
            if (element.freeShippingUrl.isNotBlank()) {
                loadImage(element.freeShippingUrl)
                show()
            } else {
                gone()
            }
        }
    }

    private fun renderShopName(element: CartWishlistItemHolderData) {
        binding.textShopName.apply {
            text = element.shopName
            val marginFour = itemView.context.resources.getDimension(R.dimen.dp_4).toInt()
            val marginNine = itemView.context.resources.getDimension(R.dimen.dp_9).toInt()
            if (binding.imgShopBadge.visibility == View.VISIBLE) {
                setMargin(marginFour, marginFour, 0, 0)
            } else {
                setMargin(marginNine, marginFour, 0, 0)
            }
        }
    }

    private fun renderShopBadge(element: CartWishlistItemHolderData) {
        binding.imgShopBadge.apply {
            if (element.badgeUrl.isNotBlank()) {
                loadImage(element.badgeUrl)
                show()
            } else {
                gone()
            }
        }
    }

    private fun renderProductPrice(element: CartWishlistItemHolderData) {
        binding.textProductPrice.apply {
            text = element.price
        }
    }

    private fun renderProductVariant(element: CartWishlistItemHolderData) {
        binding.textProductVariant.apply {
            if (element.variant.isNotBlank()) {
                text = element.variant
                show()
            } else {
                gone()
            }
        }
    }

    private fun renderProductName(element: CartWishlistItemHolderData) {
        binding.textProductName.apply {
            text = element.name
        }
    }

    private fun renderProductImage(element: CartWishlistItemHolderData) {
        binding.imgProduct.apply {
            if (element.imageUrl.isNotBlank()) {
                loadImage(element.imageUrl)
                show()
            }
        }
    }

}
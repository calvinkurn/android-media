package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_product_wishlist.view.*

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartWishlistItemViewHolder(val view: View, val actionListener: ActionListener?, val itemWidth: Int) : RecyclerView.ViewHolder(view) {

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
        itemView.btn_add_to_cart?.apply {
            setOnClickListener {
                actionListener?.onButtonAddToCartClicked(element)
            }
        }
    }

    private fun renderButtonDelete(element: CartWishlistItemHolderData) {
        itemView.btn_delete_wishlist?.apply {
            setOnClickListener {
                actionListener?.onRemoveWishlistFromWishlist(element.id)
            }
        }
    }

    private fun renderFreeShipping(element: CartWishlistItemHolderData) {
        itemView.img_free_shipping?.apply {
            if (element.freeShipping && element.freeShippingUrl.isNotBlank()) {
                setImageUrl(element.freeShippingUrl)
                show()
            } else {
                gone()
            }
        }
    }

    private fun renderShopName(element: CartWishlistItemHolderData) {
        itemView.text_shop_name?.apply {
            text = element.shopName
            val marginFour = itemView.context.resources.getDimension(R.dimen.dp_4).toInt()
            val marginNine = itemView.context.resources.getDimension(R.dimen.dp_9).toInt()
            if (itemView.img_shop_badge?.visibility == View.VISIBLE) {
                setMargin(marginFour, marginFour, 0, 0)
            } else {
                setMargin(marginNine, marginFour, 0, 0)
            }
        }
    }

    private fun renderShopBadge(element: CartWishlistItemHolderData) {
        itemView.img_shop_badge?.apply {
            if (element.badgeUrl.isNotBlank()) {
                setImageUrl(element.badgeUrl)
                show()
            } else {
                gone()
            }
        }
    }

    private fun renderProductPrice(element: CartWishlistItemHolderData) {
        itemView.text_product_price?.apply {
            text = element.price
        }
    }

    private fun renderProductVariant(element: CartWishlistItemHolderData) {
        itemView.text_product_variant?.apply {
            if (element.variant.isNotBlank()) {
                text = element.variant
                show()
            } else {
                gone()
            }
        }
    }

    private fun renderProductName(element: CartWishlistItemHolderData) {
        itemView.text_product_name?.apply {
            text = element.name
        }
    }

    private fun renderProductImage(element: CartWishlistItemHolderData) {
        itemView.img_product?.apply {
            if (element.imageUrl.isNotBlank()) {
                setImageUrl(element.imageUrl)
                show()
            }
        }
    }

}
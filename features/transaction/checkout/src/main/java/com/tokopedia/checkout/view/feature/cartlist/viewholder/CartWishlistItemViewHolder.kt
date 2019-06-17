package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartWishlistItemHolderData
import kotlinx.android.synthetic.main.item_cart_recent_view_item.view.*

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartWishlistItemViewHolder(val view: View, val actionListener: ActionListener, val itemWidth: Int) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_cart_wishlist_item
    }

    fun bind(element: CartWishlistItemHolderData) {
        if (element.isWishlist) {
            itemView.img_wishlist.setImageResource(R.drawable.ic_wishlist_checkout_on)
        } else {
            itemView.img_wishlist.setImageResource(R.drawable.ic_wishlist_checkout_off)
        }

        itemView.tv_product_name.text = element.name
        itemView.tv_product_price.text = element.price
        ImageHandler.loadImage(itemView.context, itemView.img_product,
                element.imageUrl, R.drawable.loading_page
        )

        itemView.img_product.layoutParams.width = itemWidth
        itemView.img_product.requestLayout()

        itemView.img_wishlist.setOnClickListener {
            if (element.isWishlist) {
                actionListener.onRemoveFromWishlist(element.id);
            } else {
                actionListener.onAddToWishlist(element.id);
            }
        }

        itemView.setOnClickListener {
        }
    }
}
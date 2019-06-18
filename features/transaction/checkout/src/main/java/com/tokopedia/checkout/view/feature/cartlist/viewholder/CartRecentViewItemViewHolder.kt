package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecentViewItemHolderData
import kotlinx.android.synthetic.main.item_cart_recent_view_item.view.*

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartRecentViewItemViewHolder(val view: View, val actionListener: ActionListener, val itemWidth: Int) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_cart_recent_view_item
    }

    fun bind(element: CartRecentViewItemHolderData) {
        if (element.isWishlist) {
            itemView.img_wishlist.setImageResource(R.drawable.ic_wishlist_checkout_on)
        } else {
            itemView.img_wishlist.setImageResource(R.drawable.ic_wishlist_checkout_off)
        }

        itemView.tv_product_name.text = element.name
        itemView.tv_product_price.text = element.price
        itemView.tv_shop_location.text = element.shopLocation

        if (element.rating > 0) {
            itemView.img_rating.setImageResource(getRatingImageResource(element.rating))
            itemView.tv_review_count.text = "(${element.reviewCount})"
            itemView.img_rating.visibility = View.VISIBLE
            itemView.tv_review_count.visibility = View.VISIBLE
        } else {
            itemView.img_rating.visibility = View.GONE
            itemView.tv_review_count.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(element.badgeUrl)) {
            ImageHandler.loadImage(itemView.context, itemView.img_badge,
                    element.badgeUrl, R.drawable.loading_page
            )
            itemView.img_badge.visibility = View.VISIBLE
            itemView.tv_badge_dot_separator.visibility = View.VISIBLE
        } else {
            itemView.img_badge.visibility = View.GONE
            itemView.tv_badge_dot_separator.visibility = View.VISIBLE
        }

        ImageHandler.loadImage(itemView.context, itemView.img_product,
                element.imageUrl, R.drawable.loading_page
        )

        itemView.img_product.layoutParams.width = itemWidth
        itemView.img_product.requestLayout()

        itemView.img_wishlist.setOnClickListener {
            if (element.isWishlist) {
                actionListener.onRemoveFromWishlist(element.id)
            } else {
                actionListener.onAddToWishlist(element.id)
            }
        }

        itemView.tv_atc.setOnClickListener {
            actionListener.onButtonAddToCartClicked(element.id, element.shopId, element.minOrder)
        }

        itemView.setOnClickListener {
            actionListener.onProductClicked(element.id)
        }

    }

    private fun getRatingImageResource(rating: Int): Int {
        when (rating) {
            1 -> return R.drawable.ic_star_one
            2 -> return R.drawable.ic_star_two
            3 -> return R.drawable.ic_star_three
            4 -> return R.drawable.ic_star_four
            5 -> return R.drawable.ic_star_five
            else -> return 0
        }
    }

}
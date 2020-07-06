package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.CartWishlistAdapter
import com.tokopedia.cart.view.uimodel.CartWishlistHolderData
import kotlinx.android.synthetic.main.item_cart_wishlist.view.*

/**
 * Created by Irfan Khoirul on 2019-05-31.
 */

class CartWishlistViewHolder(val view: View, val listener: ActionListener?) : RecyclerView.ViewHolder(view) {

    var wishlistAdapter: CartWishlistAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_cart_wishlist
    }

    fun bind(element: CartWishlistHolderData) {
        if (wishlistAdapter == null) {
            wishlistAdapter = CartWishlistAdapter(listener)
        }
        wishlistAdapter?.wishlistItemHoldeDataList = element.wishList
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        itemView.rv_wishlist.layoutManager = layoutManager
        itemView.rv_wishlist.adapter = wishlistAdapter
        itemView.rv_wishlist.scrollToPosition(element.lastFocussPosition)
        if (!element.hasSentImpressionAnalytics) {
            listener?.onWishlistImpression()
            element.hasSentImpressionAnalytics = true
        }
    }

}
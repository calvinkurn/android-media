package com.tokopedia.purchase_platform.cart.view.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.purchase_platform.cart.view.ActionListener
import com.tokopedia.purchase_platform.cart.view.adapter.CartWishlistAdapter
import com.tokopedia.purchase_platform.cart.view.viewmodel.CartWishlistHolderData
import kotlinx.android.synthetic.main.item_cart_wishlist.view.*

/**
 * Created by Irfan Khoirul on 2019-05-31.
 */

class CartWishlistViewHolder(val view: View, val listener: ActionListener) : RecyclerView.ViewHolder(view) {

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
    }

}
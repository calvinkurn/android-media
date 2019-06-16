package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartWishlistAdapter
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartWishlistHolderData
import kotlinx.android.synthetic.main.item_cart_wishlist.view.*

/**
 * Created by Irfan Khoirul on 2019-05-31.
 */

class CartWishlistViewHolder(val view: View, val listener: ActionListener) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_cart_wishlist
    }

    fun bind(element: CartWishlistHolderData) {
        val wishlistAdapter = CartWishlistAdapter(listener)
        wishlistAdapter.wishlistItemHoldeDataList = element.wishList
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        itemView.rv_wishlist.layoutManager = layoutManager
        itemView.rv_wishlist.adapter = wishlistAdapter
    }

}
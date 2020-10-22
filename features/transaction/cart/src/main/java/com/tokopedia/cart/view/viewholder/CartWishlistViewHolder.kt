package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.wishlist.CartWishlistAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
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
        if (element.hasInitializeRecyclerView) {
            updateList(element)
        } else {
            initializeRecyclerView(element)
        }
    }

    private fun initializeRecyclerView(element: CartWishlistHolderData) {
        if (wishlistAdapter == null) {
            wishlistAdapter = CartWishlistAdapter(listener)
        }
        wishlistAdapter?.updateWishlistItems(element.wishList)

        itemView.rv_wishlist?.apply {
            val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            setLayoutManager(layoutManager)
            adapter = wishlistAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            val itemDecorationCount = itemDecorationCount
            if (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }
            addItemDecoration(CartHorizontalItemDecoration())
            listener?.onWishlistImpression()
            element.hasInitializeRecyclerView = true
        }
    }

    private fun updateList(element: CartWishlistHolderData) {
        itemView.rv_wishlist?.apply {
            (adapter as CartWishlistAdapter).updateWishlistItems(element.wishList)
            scrollToPosition(0)
        }
    }

}
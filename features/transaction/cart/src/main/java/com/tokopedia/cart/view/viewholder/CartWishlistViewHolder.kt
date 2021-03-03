package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartWishlistBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.wishlist.CartWishlistAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.CartWishlistHolderData

/**
 * Created by Irfan Khoirul on 2019-05-31.
 */

class CartWishlistViewHolder(private val binding: ItemCartWishlistBinding, val listener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    var wishlistAdapter: CartWishlistAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_cart_wishlist
    }

    fun bind(element: CartWishlistHolderData) {
        if (element.hasInitializeRecyclerView && wishlistAdapter != null) {
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

        binding.rvWishlist.apply {
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
        binding.rvWishlist.apply {
            adapter?.let {
                (it as CartWishlistAdapter).updateWishlistItems(element.wishList)
                scrollToPosition(0)
            }
        }
    }

}
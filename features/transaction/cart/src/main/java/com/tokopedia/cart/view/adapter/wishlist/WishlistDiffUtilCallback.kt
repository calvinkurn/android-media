package com.tokopedia.cart.view.adapter.wishlist

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData

class WishlistDiffUtilCallback(private val oldList: List<CartWishlistItemHolderData>,
                               private val newList: List<CartWishlistItemHolderData>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Just return true because id comparison on `areItemsTheSame` is enough
        return true
    }

}

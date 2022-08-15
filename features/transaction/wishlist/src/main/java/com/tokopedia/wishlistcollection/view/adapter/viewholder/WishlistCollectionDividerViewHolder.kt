package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.WishlistCollectionDividerItemBinding
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData

class WishlistCollectionDividerViewHolder(private val binding: WishlistCollectionDividerItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistCollectionTypeLayoutData) {}
}
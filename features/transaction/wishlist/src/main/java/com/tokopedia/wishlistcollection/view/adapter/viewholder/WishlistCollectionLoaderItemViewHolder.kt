package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.wishlist.databinding.CollectionWishlistLoaderItemBinding

class WishlistCollectionLoaderItemViewHolder(private val binding: CollectionWishlistLoaderItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.cardLoaderItem.visible()
    }
}

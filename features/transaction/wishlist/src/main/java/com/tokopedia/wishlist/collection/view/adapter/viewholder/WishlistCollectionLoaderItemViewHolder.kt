package com.tokopedia.wishlist.collection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.wishlist.databinding.CollectionWishlistLoaderItemBinding

class WishlistCollectionLoaderItemViewHolder(private val binding: CollectionWishlistLoaderItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.cardLoaderItem.cardType = CardUnify2.TYPE_CLEAR
        binding.cardLoaderItem.visible()
    }
}

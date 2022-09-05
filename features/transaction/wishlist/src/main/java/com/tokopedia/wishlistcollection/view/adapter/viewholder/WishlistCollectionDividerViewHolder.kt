package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.wishlist.databinding.WishlistCollectionDividerItemBinding

class WishlistCollectionDividerViewHolder(private val binding: WishlistCollectionDividerItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.root.visible()
        binding.root.setPadding((-8).toDp(), 0, (-8).toDp(), 0)
    }
}
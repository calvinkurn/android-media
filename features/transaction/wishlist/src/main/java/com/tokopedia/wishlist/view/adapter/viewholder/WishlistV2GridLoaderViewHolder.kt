package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.WishlistV2LoaderGridItemBinding

class WishlistV2GridLoaderViewHolder(private val binding: WishlistV2LoaderGridItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.clLoaderGridItem.visibility = View.VISIBLE
    }
}
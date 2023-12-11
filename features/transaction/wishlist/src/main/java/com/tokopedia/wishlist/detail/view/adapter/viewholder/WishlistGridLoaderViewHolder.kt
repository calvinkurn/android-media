package com.tokopedia.wishlist.detail.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.WishlistLoaderGridItemBinding

class WishlistGridLoaderViewHolder(private val binding: WishlistLoaderGridItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.clLoaderGridItem.visibility = View.VISIBLE
    }
}

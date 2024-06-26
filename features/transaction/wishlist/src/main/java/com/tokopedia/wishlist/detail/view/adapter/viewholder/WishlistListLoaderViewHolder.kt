package com.tokopedia.wishlist.detail.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.WishlistLoaderListItemBinding

class WishlistListLoaderViewHolder(private val binding: WishlistLoaderListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.clLoaderListItem.visibility = View.VISIBLE
    }
}

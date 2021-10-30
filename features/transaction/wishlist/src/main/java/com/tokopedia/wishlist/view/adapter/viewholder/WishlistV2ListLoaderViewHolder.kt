package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2LoaderListItemBinding

class WishlistV2ListLoaderViewHolder(private val binding: WishlistV2LoaderListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData) {
        binding.clLoader.visibility = View.VISIBLE
    }
}
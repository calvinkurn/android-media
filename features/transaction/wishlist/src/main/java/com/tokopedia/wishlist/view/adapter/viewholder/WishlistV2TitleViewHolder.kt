package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationTitleItemBinding

class WishlistV2TitleViewHolder(private val binding: WishlistV2RecommendationTitleItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: WishlistV2TypeLayoutData) {
        binding.tvRvTitleWishlistV2.text = data.dataObject as String
    }
}
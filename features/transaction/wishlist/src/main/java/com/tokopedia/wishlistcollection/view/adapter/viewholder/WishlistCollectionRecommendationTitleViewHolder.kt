package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationTitleItemBinding
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData

class WishlistCollectionRecommendationTitleViewHolder(private val binding: WishlistV2RecommendationTitleItemBinding, private val isWithMargin: Boolean) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: WishlistCollectionTypeLayoutData) {
        binding.root.visible()
        binding.tvRvTitleWishlistV2.text = data.dataObject as String
    }
}
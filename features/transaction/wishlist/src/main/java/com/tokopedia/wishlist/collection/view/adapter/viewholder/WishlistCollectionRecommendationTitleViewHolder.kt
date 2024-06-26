package com.tokopedia.wishlist.collection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.wishlist.collection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistRecommendationTitleItemBinding

class WishlistCollectionRecommendationTitleViewHolder(private val binding: WishlistRecommendationTitleItemBinding, private val isWithMargin: Boolean) : RecyclerView.ViewHolder(binding.root) {

    private companion object {
        private const val PADDING_8 = 8
        private const val PADDING_12 = 12
    }

    fun bind(data: WishlistCollectionTypeLayoutData) {
        binding.root.visible()
        binding.root.setPadding(0, 0, 0, PADDING_12.toPx())
        binding.tvRvTitleWishlistV2.text = data.dataObject as String
    }
}

package com.tokopedia.wishlistcommon.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionMainSectionTextItemBinding
import com.tokopedia.wishlistcommon.data.BottomSheetWishlistCollectionTypeLayoutData

class BottomSheetWishlistCollectionMainItemViewHolder(private val binding: AddWishlistCollectionMainSectionTextItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BottomSheetWishlistCollectionTypeLayoutData) {
        if (item.dataObject is String) {
            binding.mainSectionText.text = item.dataObject
        }
    }

}
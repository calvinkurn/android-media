package com.tokopedia.wishlistcommon.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionAdditionalSectionTextItemBinding
import com.tokopedia.wishlistcommon.data.BottomSheetWishlistCollectionTypeLayoutData

class BottomSheetWishlistCollectionAdditionalItemViewHolder(
    private val binding: AddWishlistCollectionAdditionalSectionTextItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BottomSheetWishlistCollectionTypeLayoutData) {
        if (item.dataObject is String) {
            binding.additionalSectionText.text = item.dataObject
        }
    }

}
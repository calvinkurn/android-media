package com.tokopedia.wishlist.collection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.AddWishlistCollectionAdditionalSectionTextItemBinding
import com.tokopedia.wishlist.collection.data.model.BottomSheetWishlistCollectionTypeLayoutData

class BottomSheetWishlistCollectionAdditionalItemViewHolder(
    private val binding: AddWishlistCollectionAdditionalSectionTextItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BottomSheetWishlistCollectionTypeLayoutData) {
        if (item.dataObject is String) {
            binding.additionalSectionText.text = item.dataObject
        }
    }
}

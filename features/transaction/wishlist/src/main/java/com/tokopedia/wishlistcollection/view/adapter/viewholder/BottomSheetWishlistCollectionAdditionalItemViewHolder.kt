package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.AddWishlistCollectionAdditionalSectionTextItemBinding

class BottomSheetWishlistCollectionAdditionalItemViewHolder(
    private val binding: AddWishlistCollectionAdditionalSectionTextItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: com.tokopedia.wishlistcollection.data.BottomSheetWishlistCollectionTypeLayoutData) {
        if (item.dataObject is String) {
            binding.additionalSectionText.text = item.dataObject
        }
    }

}
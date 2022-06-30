package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.AddWishlistCollectionMainSectionTextItemBinding
import com.tokopedia.wishlistcollection.data.BottomSheetWishlistCollectionTypeLayoutData

class BottomSheetWishlistCollectionMainItemViewHolder(private val binding: AddWishlistCollectionMainSectionTextItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BottomSheetWishlistCollectionTypeLayoutData) {
        if (item.dataObject is String) {
            binding.mainSectionText.text = item.dataObject
        }
    }

}
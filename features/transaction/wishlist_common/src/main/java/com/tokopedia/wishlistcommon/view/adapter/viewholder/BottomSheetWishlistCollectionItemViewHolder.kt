package com.tokopedia.wishlistcommon.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionItemBinding
import com.tokopedia.wishlistcommon.data.BottomSheetWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcommon.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcommon.view.adapter.BottomSheetCollectionWishlistAdapter

class BottomSheetWishlistCollectionItemViewHolder(
    private val binding: AddWishlistCollectionItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BottomSheetWishlistCollectionTypeLayoutData) {
        if (item.dataObject is GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet.Data.MainSection.CollectionsItem) {
            binding.collectionItemImage.setImageUrl(item.dataObject.imageUrl)
            binding.mainCollectionItemName.text = item.dataObject.name
            binding.mainCollectionTotalItem.text = "${item.dataObject.totalItem} ${item.dataObject.itemText}"
            // binding.root.setOnClickListener { actionListener?.onCollectionItemClicked() }
        }
    }
}
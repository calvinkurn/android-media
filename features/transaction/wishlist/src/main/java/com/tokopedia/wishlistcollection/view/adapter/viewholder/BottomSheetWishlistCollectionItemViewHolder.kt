package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.wishlist.databinding.AddWishlistCollectionItemBinding
import com.tokopedia.wishlistcollection.data.model.BottomSheetWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcollection.view.adapter.BottomSheetCollectionWishlistAdapter

class BottomSheetWishlistCollectionItemViewHolder(
    private val binding: AddWishlistCollectionItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: BottomSheetWishlistCollectionTypeLayoutData,
        actionListener: BottomSheetCollectionWishlistAdapter.ActionListener?
    ) {
        if (item.dataObject is GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet.Data.MainSection.CollectionsItem) {
            if (item.dataObject.label.isNotEmpty()) {
                binding.labelCollectionItem.visible()
                binding.labelCollectionItem.text = item.dataObject.label
            } else {
                binding.labelCollectionItem.gone()
            }
            binding.collectionItemImage.setImageUrl(item.dataObject.imageUrl)
            binding.mainCollectionItemName.text = item.dataObject.name
            binding.mainCollectionTotalItem.text =
                "${item.dataObject.totalItem} ${item.dataObject.itemText}"

            if (item.dataObject.isContainProduct) {
                binding.icCheck.visible()
            } else {
                binding.icCheck.gone()
            }

            binding.root.setOnClickListener { actionListener?.onCollectionItemClicked(item.dataObject.name, item.dataObject.id) }
        }
    }
}
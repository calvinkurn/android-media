package com.tokopedia.wishlistcommon.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionCreateNewItemBinding
import com.tokopedia.wishlistcommon.data.AddToWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcommon.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.IMAGE_URL_SMALL_CREATE_NEW
import com.tokopedia.wishlistcommon.view.adapter.BottomSheetCollectionWishlistAdapter

class BottomSheetCollectionWishlistCreateItemViewHolder(
    private val binding: AddWishlistCollectionCreateNewItemBinding,
    private val actionListener: BottomSheetCollectionWishlistAdapter.ActionListener?
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AddToWishlistCollectionTypeLayoutData) {
        if (item.dataObject is GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet.Data.Placeholder) {
            binding.createCollectionImage.setImageUrl(IMAGE_URL_SMALL_CREATE_NEW)
            binding.createCollectionLabel.text = item.dataObject.text
            binding.root.setOnClickListener { actionListener?.onCollectionItemClicked() }
        }
    }
}
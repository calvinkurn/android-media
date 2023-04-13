package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.wishlist.databinding.AddWishlistCollectionCreateNewItemBinding
import com.tokopedia.wishlistcollection.data.model.BottomSheetWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.view.adapter.BottomSheetWishlistCollectionAdapter
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.IMAGE_URL_SMALL_CREATE_NEW

class BottomSheetWishlistCollectionCreateItemViewHolder(
    private val binding: AddWishlistCollectionCreateNewItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: BottomSheetWishlistCollectionTypeLayoutData,
        actionListener: BottomSheetWishlistCollectionAdapter.ActionListener?
    ) {
        if (item.dataObject is com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet.Data) {
            binding.createCollectionImage.setImageUrl(IMAGE_URL_SMALL_CREATE_NEW)
            binding.createCollectionLabel.text = HtmlLinkHelper(itemView.context, item.dataObject.placeholder.text).spannedString
            binding.root.setOnClickListener {
                actionListener?.onCreateNewCollectionClicked(item.dataObject)
            }
        }
    }
}

package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.wishlist.databinding.AddWishlistCollectionCreateNewItemBinding
import com.tokopedia.wishlistcollection.data.BottomSheetWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.view.adapter.BottomSheetCollectionWishlistAdapter
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.IMAGE_URL_SMALL_CREATE_NEW

class BottomSheetWishlistCollectionCreateItemViewHolder(
    private val binding: AddWishlistCollectionCreateNewItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: BottomSheetWishlistCollectionTypeLayoutData,
        actionListener: BottomSheetCollectionWishlistAdapter.ActionListener?
    ) {
        if (item.dataObject is com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet.Data.Placeholder) {
            binding.createCollectionImage.setImageUrl(IMAGE_URL_SMALL_CREATE_NEW)
            binding.createCollectionLabel.text = HtmlLinkHelper(itemView.context, item.dataObject.text).spannedString
            binding.root.setOnClickListener {
                println("++ udah masuk sini sih")
                actionListener?.onCreateNewCollectionClicked()
            }
        }
    }
}
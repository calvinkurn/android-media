package com.tokopedia.wishlistcommon.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionCreateNewItemBinding
import com.tokopedia.wishlistcommon.data.BottomSheetWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcommon.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.IMAGE_URL_SMALL_CREATE_NEW
import com.tokopedia.wishlistcommon.view.adapter.BottomSheetCollectionWishlistAdapter

class BottomSheetWishlistCollectionCreateItemViewHolder(
    private val binding: AddWishlistCollectionCreateNewItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BottomSheetWishlistCollectionTypeLayoutData) {
        if (item.dataObject is GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet.Data.Placeholder) {
            binding.createCollectionImage.setImageUrl(IMAGE_URL_SMALL_CREATE_NEW)
            binding.createCollectionLabel.text = HtmlLinkHelper(itemView.context, item.dataObject.text).spannedString
        }
    }
}
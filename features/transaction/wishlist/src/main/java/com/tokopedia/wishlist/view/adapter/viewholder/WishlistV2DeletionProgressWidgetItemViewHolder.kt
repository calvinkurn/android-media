package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.response.CountDeletionWishlistV2Response
import com.tokopedia.wishlist.databinding.WishlistV2CountDeletionItemBinding

class WishlistV2DeletionProgressWidgetItemViewHolder(private val binding: WishlistV2CountDeletionItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData) {
        if (item.dataObject is CountDeletionWishlistV2Response.Data) {
            binding.run {
                wishlistV2CountDeletionMessage.text = item.dataObject.message
                wishlistV2CountDeletionProgressbar.setValue(item.dataObject.successfullyRemovedItems, true)
            }
        }
    }
}
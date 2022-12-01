package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.databinding.WishlistV2CountDeletionItemBinding
import com.tokopedia.wishlist.R as Rv2

class WishlistV2DeletionProgressWidgetItemViewHolder(private val binding: WishlistV2CountDeletionItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData) {
        if (item.dataObject is DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress) {
            binding.run {
                var message = itemView.context.getString(Rv2.string.wishlist_v2_default_message_deletion_progress)
                if (item.dataObject.message.isNotEmpty()) message = item.dataObject.message
                wishlistV2CountDeletionMessage.text = message
                wishlistV2LabelProgressBar.text = "${item.dataObject.successfullyRemovedItems}/${item.dataObject.totalItems}"

                val indicatorProgressBar = (item.dataObject.successfullyRemovedItems/item.dataObject.totalItems)*100
                wishlistV2CountDeletionProgressbar.setValue(indicatorProgressBar, false)
            }
        }
    }
}
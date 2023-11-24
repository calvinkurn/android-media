package com.tokopedia.wishlist.detail.view.adapter.viewholder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.WishlistCountDeletionItemBinding
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.detail.util.WishlistConsts.INDICATOR_PROGRESS_BAR_PERCENTAGE
import com.tokopedia.wishlist.R as wishlistR

class WishlistDeletionProgressWidgetItemViewHolder(private val binding: WishlistCountDeletionItemBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(item: WishlistTypeLayoutData) {
        if (item.dataObject is DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress) {
            binding.run {
                var message = itemView.context.getString(wishlistR.string.wishlist_v2_default_message_deletion_progress)
                if (item.dataObject.message.isNotEmpty()) message = item.dataObject.message
                wishlistV2CountDeletionMessage.text = message
                wishlistV2LabelProgressBar.text = "${item.dataObject.successfullyRemovedItems}/${item.dataObject.totalItems}"

                val indicatorProgressBar = (item.dataObject.successfullyRemovedItems / item.dataObject.totalItems) * INDICATOR_PROGRESS_BAR_PERCENTAGE
                wishlistV2CountDeletionProgressbar.setValue(indicatorProgressBar, false)
            }
        }
    }
}

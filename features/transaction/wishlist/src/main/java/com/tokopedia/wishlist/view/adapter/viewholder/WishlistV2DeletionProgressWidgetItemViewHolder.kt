package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.response.CountDeletionWishlistV2Response
import com.tokopedia.wishlist.databinding.WishlistV2CountDeletionItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts

class WishlistV2DeletionProgressWidgetItemViewHolder(private val binding: WishlistV2CountDeletionItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData, wishlistItemTypeLayout: String) {
        if (item.dataObject is CountDeletionWishlistV2Response.Data) {
            binding.run {
                val params = (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
                    if (wishlistItemTypeLayout == WishlistV2Consts.TYPE_LIST) {
                        setMargins(10.toPx(), 0, 10.toPx(), 0)
                    } else {
                        setMargins(0, 0, 0, 0)
                    }
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    isFullSpan = true
                }
                binding.root.layoutParams = params
                wishlistV2CountDeletionMessage.text = item.dataObject.message
                wishlistV2CountDeletionProgressbar.setValue(item.dataObject.successfullyRemovedItems, true)
            }
        }
    }
}
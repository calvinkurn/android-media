package com.tokopedia.wishlist.detail.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.WishlistEmptyStateNotFoundItemBinding
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter

class WishlistEmptyStateNotFoundViewHolder(private val binding: WishlistEmptyStateNotFoundItemBinding, private val actionListener: WishlistAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistTypeLayoutData) {
        if (item.dataObject is String) {
            binding.emptyState.emptyStateDescriptionID.text = itemView.context.getString(R.string.empty_state_not_found_description, item.dataObject)
            binding.emptyState.setPrimaryCTAClickListener {
                actionListener?.onNotFoundButtonClicked(item.dataObject)
            }
        }
    }
}

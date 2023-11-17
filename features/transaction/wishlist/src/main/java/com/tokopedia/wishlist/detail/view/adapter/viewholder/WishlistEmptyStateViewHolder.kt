package com.tokopedia.wishlist.detail.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.WishlistEmptyStateItemBinding
import com.tokopedia.wishlist.detail.data.model.WishlistEmptyStateData
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter

class WishlistEmptyStateViewHolder(private val binding: WishlistEmptyStateItemBinding, private val actionListener: WishlistAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistTypeLayoutData) {
        if (item.dataObject is WishlistEmptyStateData) {
            binding.wishlistV2EmptyState.apply {
                setImageUrl(itemView.context.getString(item.dataObject.img))
                setTitle(itemView.context.getString(item.dataObject.title))
                setDescription(itemView.context.getString(item.dataObject.desc))
                setPrimaryCTAText(itemView.context.getString(item.dataObject.btnText))
                setPrimaryCTAClickListener { actionListener?.onResetFilter() }
            }
        }
    }
}

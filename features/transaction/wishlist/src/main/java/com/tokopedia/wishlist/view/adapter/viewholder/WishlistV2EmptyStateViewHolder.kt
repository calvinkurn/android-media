package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2EmptyStateData
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2EmptyStateItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2EmptyStateViewHolder(private val binding: WishlistV2EmptyStateItemBinding, private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistV2TypeLayoutData) {
        if (item.dataObject is WishlistV2EmptyStateData) {
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
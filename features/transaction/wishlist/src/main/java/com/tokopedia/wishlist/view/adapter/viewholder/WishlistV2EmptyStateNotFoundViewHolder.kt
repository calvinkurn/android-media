package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2EmptyStateNotFoundItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2EmptyStateNotFoundViewHolder(private val binding: WishlistV2EmptyStateNotFoundItemBinding, private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistV2TypeLayoutData) {
        binding.emptyStateNotFoundImg.setImageUrl(itemView.context.getString(R.string.empty_state_img_not_found))
        binding.emptyStateNotFoundDescriptionText.text = itemView.context.getString(R.string.empty_state_not_found_description, item.dataObject as String)
        binding.emptyStateNotFoundButton.setOnClickListener {
            // actionListener.onNotFoundButtonClicked()
        }
    }
}
package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.WishlistCollectionEditOptionItemBinding
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionByIdResponse
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionEditAdapter

class WishlistCollectionEditOptionItemViewHolder(private val binding: WishlistCollectionEditOptionItemBinding,
                                                 private val listener: WishlistCollectionEditAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        actionItem: GetWishlistCollectionByIdResponse.GetWishlistCollectionById.Data.AccessOptionsItem
    ) {
        binding.run {
            optionAccessLabel.text = actionItem.name
            optionAccessName.text = actionItem.description
            root?.setOnClickListener { listener?.onOptionAccessItemClicked(actionItem.id) }
        }
    }
}

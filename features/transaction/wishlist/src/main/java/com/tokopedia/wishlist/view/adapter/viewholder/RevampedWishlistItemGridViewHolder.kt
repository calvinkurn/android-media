package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.RevampedWishlistTypeData
import com.tokopedia.wishlist.databinding.RevampedWishlistGridItemBinding
import com.tokopedia.wishlist.view.adapter.RevampedWishlistAdapter

/**
 * Created by fwidjaja on 15/10/21.
 */
class RevampedWishlistItemGridViewHolder (private val binding: RevampedWishlistGridItemBinding,
                                          private val actionListener: RevampedWishlistAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RevampedWishlistTypeData, position: Int) {

    }

}
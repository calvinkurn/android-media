package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2GridItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

/**
 * Created by fwidjaja on 15/10/21.
 */
class WishlistV2GridItemViewHolder (private val binding: WishlistV2GridItemBinding,
                                    private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData, position: Int) {

    }

}
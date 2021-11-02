package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2ListItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2ListItemViewHolder(private val binding: WishlistV2ListItemBinding,
                                   private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData, position: Int, isShowCheckbox: Boolean) {
        binding.wishlistItem.setProductModel(item.dataObject as ProductCardModel)

        if (isShowCheckbox) {
            binding.wishlistCheckbox.visibility = View.VISIBLE
        } else {
            binding.wishlistCheckbox.visibility = View.GONE
        }

        binding.wishlistItem.setSecondaryButtonClickListener { actionListener?.onThreeDotsMenuClicked(item.wishlistItem) }
    }

}
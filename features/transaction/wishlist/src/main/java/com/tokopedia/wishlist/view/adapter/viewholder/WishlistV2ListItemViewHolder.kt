package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2ListItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

/**
 * Created by fwidjaja on 14/10/21.
 */
class WishlistV2ListItemViewHolder(private val binding: WishlistV2ListItemBinding,
                                   private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData, position: Int) {
        binding.wishlistItem.setProductModel(item.dataObject as ProductCardModel)
        if (position % 2 == 0) {
            binding.wishlistItem.getNotifyMeButton()?.text = "+ Keranjang"
        } else {
            binding.wishlistItem.getNotifyMeButton()?.text = "Lihat Barang Serupa"
        }
    }

}
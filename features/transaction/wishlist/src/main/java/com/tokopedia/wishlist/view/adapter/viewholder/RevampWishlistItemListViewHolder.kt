package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.wishlist.data.model.RevampedWishlistTypeData
import com.tokopedia.wishlist.databinding.RevampedWishlistListItemBinding
import com.tokopedia.wishlist.view.adapter.RevampedWishlistAdapter

/**
 * Created by fwidjaja on 14/10/21.
 */
class RevampWishlistItemListViewHolder(private val binding: RevampedWishlistListItemBinding,
                                       private val actionListener: RevampedWishlistAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RevampedWishlistTypeData, position: Int) {
        binding.wishlistItem.setProductModel(item.dataObject as ProductCardModel)
        if (position % 2 == 0) {
            binding.wishlistItem.getNotifyMeButton()?.text = "+ Keranjang"
        } else {
            binding.wishlistItem.getNotifyMeButton()?.text = "Lihat Barang Serupa"
        }
    }

}
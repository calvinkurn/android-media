package com.tokopedia.wishlist.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.BottomsheetWishlistThreeDotsMenuItemBinding
import com.tokopedia.wishlist.detail.data.model.WishlistUiModel
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistThreeDotsMenuItemViewHolder
import com.tokopedia.wishlist.detail.view.bottomsheet.BottomSheetThreeDotsMenuWishlist

class BottomSheetThreeDotsMenuWishlistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: BottomSheetThreeDotsMenuWishlist.BottomSheetListener? = null
    var wishlistItem = WishlistUiModel.Item()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = BottomsheetWishlistThreeDotsMenuItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
        return WishlistThreeDotsMenuItemViewHolder(binding, actionListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WishlistThreeDotsMenuItemViewHolder -> {
                holder.bind(wishlistItem, wishlistItem.buttons.additionalButtons[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return wishlistItem.buttons.additionalButtons.size
    }

    fun setActionListener(listener: BottomSheetThreeDotsMenuWishlist.BottomSheetListener) {
        this.actionListener = listener
    }
}

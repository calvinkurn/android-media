package com.tokopedia.wishlist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2Response
import com.tokopedia.wishlist.databinding.BottomsheetWishlistV2ThreeDotsMenuItemBinding
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2ThreeDotsMenuItemViewHolder
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2ThreeDotsMenuBottomSheet

/**
 * Created by fwidjaja on 29/10/21.
 */
class WishlistV2ThreeDotsMenuBottomSheetAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: WishlistV2ThreeDotsMenuBottomSheet.BottomSheetListener? = null
    var listThreeDotsMenuItem = listOf<WishlistV2Response.Data.WishlistV2.Item.Buttons.AdditionalButtonsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = BottomsheetWishlistV2ThreeDotsMenuItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
        return WishlistV2ThreeDotsMenuItemViewHolder(binding, actionListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WishlistV2ThreeDotsMenuItemViewHolder -> {
                holder.bind(listThreeDotsMenuItem[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return listThreeDotsMenuItem.size
    }

    fun setActionListener(listener: WishlistV2ThreeDotsMenuBottomSheet.BottomSheetListener) {
        this.actionListener = listener
    }
}
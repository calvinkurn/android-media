package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.WishlistV2Response
import com.tokopedia.wishlist.databinding.BottomsheetWishlistV2ThreeDotsMenuItemBinding
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2ThreeDotsMenuBottomSheet

class WishlistV2ThreeDotsMenuItemViewHolder (private val binding: BottomsheetWishlistV2ThreeDotsMenuItemBinding,
                                             private val listener: WishlistV2ThreeDotsMenuBottomSheet.BottomSheetListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(additionalButtonsItem: WishlistV2Response.Data.WishlistV2.ItemsItem.Buttons.AdditionalButtonsItem) {
        binding.menuItem.text = additionalButtonsItem.text
        binding.root.setOnClickListener {
            listener?.onThreeDotsMenuItemSelected(additionalButtonsItem)
        }
    }
}
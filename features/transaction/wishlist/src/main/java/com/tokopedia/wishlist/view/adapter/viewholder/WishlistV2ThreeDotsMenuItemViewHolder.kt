package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2UiModel
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.databinding.BottomsheetWishlistV2ThreeDotsMenuItemBinding
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2ThreeDotsMenuBottomSheet

class WishlistV2ThreeDotsMenuItemViewHolder (private val binding: BottomsheetWishlistV2ThreeDotsMenuItemBinding,
                                             private val listener: WishlistV2ThreeDotsMenuBottomSheet.BottomSheetListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(wishlistItem: WishlistV2UiModel.Item, additionalButtonsItem: WishlistV2UiModel.Item.Buttons.AdditionalButtonsItem) {
        binding.menuItem.text = additionalButtonsItem.text
        binding.root.setOnClickListener {
            listener?.onThreeDotsMenuItemSelected(wishlistItem, additionalButtonsItem)
        }
    }
}
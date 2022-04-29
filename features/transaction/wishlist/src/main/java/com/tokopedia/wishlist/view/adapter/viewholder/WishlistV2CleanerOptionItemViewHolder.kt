package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.databinding.BottomsheetWishlistStorageCleanerItemBinding

class WishlistV2CleanerOptionItemViewHolderOld(private val binding: BottomsheetWishlistStorageCleanerItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        optionCleanerItem: WishlistV2Response.Data.WishlistV2.StorageCleanerBottomSheet.OptionCleanerBottomsheet,
        isHideDivider: Boolean,
        adapterPosition: Int
    ) {
        binding.wishlistCleanerOptionTitle.text = MethodChecker.fromHtml(optionCleanerItem.name)
        binding.wishlistCleanerOptionDesc.text = MethodChecker.fromHtml(optionCleanerItem.description)

        if (isHideDivider) binding.wishlistCleanerDivider.gone()
        else binding.wishlistCleanerDivider.visible()

        if (adapterPosition == 0) binding.wishlistCleanerOptionIconCheck.visible()
        else binding.wishlistCleanerOptionIconCheck.gone()

        binding.root.setOnClickListener {
            // listener?.onOptionItemClicked(adapterPosition)
        }
    }
}
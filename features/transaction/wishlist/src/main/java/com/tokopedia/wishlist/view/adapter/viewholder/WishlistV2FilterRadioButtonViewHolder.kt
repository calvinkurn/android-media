package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterRadioButtonItemBinding
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet

class WishlistV2FilterRadioButtonViewHolder(
    private val binding: BottomsheetWishlistFilterRadioButtonItemBinding,
    private val listener: WishlistV2FilterBottomSheet.BottomSheetListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(parentFilterName: String, label: String, optionId: String, selected: Boolean) {
        binding.labelOption.text = label
        binding.rbOption.isChecked = selected
        binding.root.setOnClickListener {
            listener?.onRadioButtonSelected(name = parentFilterName, optionId = optionId, label = label)
        }
        binding.rbOption.setOnClickListener {
            listener?.onRadioButtonSelected(name = parentFilterName, optionId = optionId, label = label)
        }
    }
}

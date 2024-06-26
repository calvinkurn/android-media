package com.tokopedia.wishlist.detail.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterRadioButtonItemBinding
import com.tokopedia.wishlist.detail.view.bottomsheet.BottomSheetFilterWishlist

class WishlistFilterRadioButtonViewHolder(
    private val binding: BottomsheetWishlistFilterRadioButtonItemBinding,
    private val listener: BottomSheetFilterWishlist.BottomSheetListener?
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

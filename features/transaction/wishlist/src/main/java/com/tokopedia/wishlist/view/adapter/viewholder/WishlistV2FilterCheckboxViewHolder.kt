package com.tokopedia.wishlist.view.adapter.viewholder

import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterCheckboxItemBinding
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet

class WishlistV2FilterCheckboxViewHolder(
    private val binding: BottomsheetWishlistFilterCheckboxItemBinding,
    private val listener: WishlistV2FilterBottomSheet.BottomSheetListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(parentFilterName: String, title: String, desc: String, optionId: String, selected: Boolean, isResetCheckbox: Boolean) {
        binding.titleOption.text = title
        binding.descOption.text = desc

        if (isResetCheckbox) {
            binding.cbOption.isChecked = false
        } else {
            binding.cbOption.isChecked = selected
        }

        binding.cbOption.setOnCheckedChangeListener(checkboxListener(parentFilterName, optionId, title))
        binding.root.setOnClickListener {
            binding.cbOption.isChecked = !binding.cbOption.isChecked
            listener?.onCheckboxSelected(parentFilterName, optionId, binding.cbOption.isChecked, title)
        }
    }

    private fun checkboxListener(parentFilterName: String, optionId: String, titleCheckbox: String): CompoundButton.OnCheckedChangeListener {
        return CompoundButton.OnCheckedChangeListener { _, isChecked ->
            listener?.onCheckboxSelected(parentFilterName, optionId, isChecked, titleCheckbox)
        }
    }
}

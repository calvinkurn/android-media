package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterRadioButtonItemBinding
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet

class WishlistV2FilterRadioButtonViewHolder(private val binding: BottomsheetWishlistFilterRadioButtonItemBinding,
                                            private val listener: WishlistV2FilterBottomSheet.BottomSheetListener?) : RecyclerView.ViewHolder(binding.root) {
    private val listChecked = arrayListOf<String>()
    fun bind(parentFilterName: String, label: String, optionId: String, selected: Boolean) {
        binding.labelOption.text = label
        binding.rbOption.isChecked = selected
        binding.root.setOnClickListener {
            listChecked.add(optionId)
            listener?.onRadioButtonSelected(WishlistV2Params.WishlistSortFilterParam(
                    name = parentFilterName,
                    selected = listChecked
            ))
        }
    }
}
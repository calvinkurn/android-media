package com.tokopedia.wishlist.view.adapter.viewholder

import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterCheckboxItemBinding
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet

/**
 * Created by fwidjaja on 20/10/21.
 */
class WishlistV2FilterCheckboxViewHolder(private val binding: BottomsheetWishlistFilterCheckboxItemBinding,
                                         private val listener: WishlistV2FilterBottomSheet.BottomSheetListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(parentFilterName: String, title: String, desc: String, optionId: String) {
        binding.titleOption.text = title
        binding.descOption.text = desc
        binding.cbOption.setOnCheckedChangeListener(checkboxListener(parentFilterName, optionId))
        binding.root.setOnClickListener {
            checkboxListener(parentFilterName, optionId)
        }
    }

    private fun checkboxListener(parentFilterName: String, optionId: String): CompoundButton.OnCheckedChangeListener? {
        return CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                listener?.onCheckboxSelected(WishlistV2Params.WishlistSortFilterParam(
                        name = parentFilterName,
                        selected = optionId
                ))
            }
        }
    }
}
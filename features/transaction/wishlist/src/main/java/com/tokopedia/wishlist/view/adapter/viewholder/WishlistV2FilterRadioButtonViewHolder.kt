package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterRadioButtonItemBinding
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet

/**
 * Created by fwidjaja on 20/10/21.
 */
class WishlistV2FilterRadioButtonViewHolder(private val binding: BottomsheetWishlistFilterRadioButtonItemBinding,
                                            private val listener: WishlistV2FilterBottomSheet.BottomSheetListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(parentFilterName: String, label: String, optionId: String) {
        binding.labelOption.text = label
        binding.root.setOnClickListener {
            listener?.onRadioButtonSelected(WishlistV2Params.WishlistSortFilterParam(
                    name = parentFilterName,
                    selected = optionId
            ))
        }
    }
}
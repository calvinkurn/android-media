package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemBottomSheetListItemBinding
import com.tokopedia.sellerhomecommon.presentation.model.BottomSheetListItemUiModel

/**
 * Created By @ilhamsuaib on 27/05/20
 */

class BottomSheetListItemViewHolder(
    view: View?
) : AbstractViewHolder<BottomSheetListItemUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_item_bottom_sheet_list_item
    }

    private val binding by lazy {
        ShcItemBottomSheetListItemBinding.bind(itemView)
    }

    override fun bind(element: BottomSheetListItemUiModel) {
        with(element) {
            binding.tvShcBottomSheetListItemTitle.text = title
            binding.tvShcBottomSheetListItemDesc.text = description
        }
    }
}
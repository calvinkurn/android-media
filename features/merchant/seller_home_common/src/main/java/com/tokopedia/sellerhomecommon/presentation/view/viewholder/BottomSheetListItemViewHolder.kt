package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemBottomSheetListItemBinding
import com.tokopedia.sellerhomecommon.presentation.model.BottomSheetListItemUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By @ilhamsuaib on 27/05/20
 */

class BottomSheetListItemViewHolder(
    view: View?
) : AbstractViewHolder<BottomSheetListItemUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_item_bottom_sheet_list_item
    }

    val binding by lazy {
        ShcItemBottomSheetListItemBinding.bind(itemView)
    }

    override fun bind(element: BottomSheetListItemUiModel) {
        with(element) {
            binding.tvShcBottomSheetListItemTitle.text = title

            if (description.isEmpty()) {
                binding.tvShcBottomSheetListItemTitle.setType(Typography.DISPLAY_1)
                binding.tvShcBottomSheetListItemTitle.setMargin(0, 8.toPx(), 0, 0)
            } else {
                binding.tvShcBottomSheetListItemTitle.setType(Typography.DISPLAY_2)
                binding.tvShcBottomSheetListItemTitle.setMargin(0, 0, 0, 0)
            }

            binding.dividerShcBottomSheetListItemDesc.showWithCondition(
                description.isNotEmpty() && !element.isLastPosition
            )
            binding.tvShcBottomSheetListItemDesc.shouldShowWithAction(description.isNotEmpty()) {
                binding.tvShcBottomSheetListItemDesc.text = description
            }
        }
    }
}
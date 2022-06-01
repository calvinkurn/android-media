package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcSortBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.SortUiModel

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class SortViewHolder(
        itemView: View?,
        private val onApplyClick: (sort: SortUiModel) -> Unit
) : AbstractViewHolder<SortUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_mvc_sort
    }

    private var binding: ItemMvcSortBinding? by viewBinding()

    override fun bind(element: SortUiModel) {
        binding?.apply {
            tvMvcSort.text = element.label

            ivCheck.isVisible = element.isSelected

            root.setOnClickListener {
                if (!ivCheck.isVisible) {
                    onApplyClick(element)
                }
            }
        }
    }
}
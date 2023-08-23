package com.tokopedia.minicart.bmgm.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.presentation.model.MiniCartDetailUiModel
import com.tokopedia.minicart.databinding.ItemBmgmMiniCartDetailSectionBinding

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartDetailSectionViewHolder(
    itemView: View
) : AbstractViewHolder<MiniCartDetailUiModel.Section>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_mini_cart_detail_section
    }

    private val binding = ItemBmgmMiniCartDetailSectionBinding.bind(itemView)

    override fun bind(element: MiniCartDetailUiModel.Section) {
        val textColorResId = if (element.isDiscountSection) {
            com.tokopedia.unifyprinciples.R.color.Unify_NN950
        } else {
            com.tokopedia.unifyprinciples.R.color.Unify_NN600
        }

        with(binding) {
            tvBmgmDetailSection.setTextColor(root.context.getResColor(textColorResId))
            tvBmgmDetailSection.text = element.sectionText.parseAsHtml()

            removeTopSpace(element)
        }
    }

    private fun removeTopSpace(element: MiniCartDetailUiModel.Section) {
        val isFirstIndex = absoluteAdapterPosition == Int.ZERO
        binding.tvBmgmDetailSection.run {
            if (isFirstIndex) {
                setPadding(paddingLeft, 0, paddingEnd, paddingBottom)
            }
            if (!element.isDiscountSection) {
                setPadding(paddingLeft, paddingTop, paddingEnd, context.dpToPx(8).toInt())
            }
        }
    }
}
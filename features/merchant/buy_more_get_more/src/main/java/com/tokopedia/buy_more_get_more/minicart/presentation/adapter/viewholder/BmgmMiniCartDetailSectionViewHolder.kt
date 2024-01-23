package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemBmgmMiniCartDetailSectionBinding
import com.tokopedia.buy_more_get_more.minicart.presentation.model.MiniCartDetailUiModel
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

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
            unifyprinciplesR.color.Unify_NN950
        } else {
            unifyprinciplesR.color.Unify_NN600
        }

        with(binding) {
            tvBmgmDetailSection.setTextColor(root.context.getResColor(textColorResId))
            tvBmgmDetailSection.text = element.sectionText.parseAsHtml()
        }
    }
}
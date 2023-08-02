package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.calculation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyCalculationSubtitleBinding
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationSubtitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyCalculationSubtitleViewHolder(view: View): AbstractViewHolder<ItemPenaltyCalculationSubtitleUiModel>(view) {

    private val binding: ItemPenaltyCalculationSubtitleBinding? by viewBinding()

    override fun bind(element: ItemPenaltyCalculationSubtitleUiModel) {
        binding?.tvPenaltyCalculationSubtitle?.text = element.title
        binding?.tvPenaltyCalculationSubtitleInfo?.text = element.desc
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_calculation_subtitle
    }

}

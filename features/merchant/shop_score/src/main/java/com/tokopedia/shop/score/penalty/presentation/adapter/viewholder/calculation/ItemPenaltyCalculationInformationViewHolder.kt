package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.calculation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyCalculationInformationBinding
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationInformationUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyCalculationInformationViewHolder(view: View) :
    AbstractViewHolder<ItemPenaltyCalculationInformationUiModel>(view) {

    private val binding: ItemPenaltyCalculationInformationBinding? by viewBinding()

    override fun bind(element: ItemPenaltyCalculationInformationUiModel) {
        binding?.tvPenaltyCalculationInformation?.text = element.description.parseAsHtml()
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_calculation_information
    }

}

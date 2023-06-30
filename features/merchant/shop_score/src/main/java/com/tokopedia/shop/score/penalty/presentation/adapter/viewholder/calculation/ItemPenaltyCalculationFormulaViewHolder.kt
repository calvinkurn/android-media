package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.calculation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyCalculationFormulaBinding
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationFormulaUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyCalculationFormulaViewHolder(view: View) :
    AbstractViewHolder<ItemPenaltyCalculationFormulaUiModel>(view) {

    private val binding: ItemPenaltyCalculationFormulaBinding? by viewBinding()

    override fun bind(element: ItemPenaltyCalculationFormulaUiModel) {
        binding?.run {
            tvPenaltyCalculationFractionNumerator.text = element.ongoingPenalty.toString()
            tvPenaltyCalculationFractionDenominator.text = element.totalOrder.toString()
            tvPenaltyCalculationFractionResult.text = element.percentageResult
            tvPenaltyCalculationFractionValue.text = element.pointResult.toString()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_calculation_formula
    }

}

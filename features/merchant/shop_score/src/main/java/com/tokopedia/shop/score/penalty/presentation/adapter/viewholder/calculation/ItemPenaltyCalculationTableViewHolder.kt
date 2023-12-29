package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.calculation

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyCalculationTableBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.ItemPenaltyCalculationTableAdapter
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationTableDetailUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationTableUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyCalculationTableViewHolder(view: View) :
    AbstractViewHolder<ItemPenaltyCalculationTableUiModel>(view) {

    private val binding: ItemPenaltyCalculationTableBinding? by viewBinding()

    override fun bind(element: ItemPenaltyCalculationTableUiModel) {
        setupTablePercentage(element.conversionList.map { it.first to it.third })
        setupTableDeduction(element.conversionList.map { it.second to it.third })
    }

    private fun setupTablePercentage(
        percentageList: List<Pair<String, Boolean>>
    ) {
        binding?.rvPenaltyCalculationTablePercentage?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ItemPenaltyCalculationTableAdapter(getTablePercentageUiModels(percentageList))
        }
    }

    private fun setupTableDeduction(
        pointsList: List<Pair<Long, Boolean>>
    ) {
        binding?.rvPenaltyCalculationTableDeduction?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ItemPenaltyCalculationTableAdapter(getTableDeductionUiModels(pointsList))
        }
    }

    private fun getTablePercentageUiModels(
        percentageList: List<Pair<String, Boolean>>
    ): List<ItemPenaltyCalculationTableDetailUiModel> {
        return percentageList.map { (percentage, isBold) ->
            ItemPenaltyCalculationTableDetailUiModel(
                percentage,
                isBold,
                false
            )
        }
    }

    private fun getTableDeductionUiModels(
        pointsList: List<Pair<Long, Boolean>>
    ): List<ItemPenaltyCalculationTableDetailUiModel> {
        return pointsList.map { (points, isBold) ->
            ItemPenaltyCalculationTableDetailUiModel(
                points.toString(),
                isBold,
                isBold
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_calculation_table
    }

}

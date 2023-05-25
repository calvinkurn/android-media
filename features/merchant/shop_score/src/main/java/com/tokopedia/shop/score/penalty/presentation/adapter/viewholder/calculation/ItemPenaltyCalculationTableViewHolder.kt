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
        setupTablePercentage(element.conversionList.map { it.first }, element.selectedIndex)
        setupTableDeduction(element.conversionList.map { it.second }, element.selectedIndex)
    }

    private fun setupTablePercentage(
        percentageList: List<String>,
        selectedIndex: Int
    ) {
        binding?.rvPenaltyCalculationTablePercentage?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ItemPenaltyCalculationTableAdapter(getTablePercentageUiModels(percentageList, selectedIndex))
        }
    }

    private fun setupTableDeduction(
        pointsList: List<Int>,
        selectedIndex: Int
    ) {
        binding?.rvPenaltyCalculationTableDeduction?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ItemPenaltyCalculationTableAdapter(getTableDeductionUiModels(pointsList, selectedIndex))
        }
    }

    private fun getTablePercentageUiModels(
        percentageList: List<String>,
        selectedIndex: Int
    ): List<ItemPenaltyCalculationTableDetailUiModel> {
        return percentageList.mapIndexed { index, percentage ->
            ItemPenaltyCalculationTableDetailUiModel(
                percentage,
                selectedIndex == index,
                false
            )
        }
    }

    private fun getTableDeductionUiModels(
        pointsList: List<Int>,
        selectedIndex: Int
    ): List<ItemPenaltyCalculationTableDetailUiModel> {
        return pointsList.mapIndexed { index, points ->
            ItemPenaltyCalculationTableDetailUiModel(
                points.toString(),
                selectedIndex == index,
                true
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_calculation_table
    }

}

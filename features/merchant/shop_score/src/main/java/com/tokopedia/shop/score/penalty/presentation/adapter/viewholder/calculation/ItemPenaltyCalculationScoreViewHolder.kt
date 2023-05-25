package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.calculation

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyCalculationScoreBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.ItemPenaltyCalculationScoreAdapter
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationScoreDetailUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationScoreUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyCalculationScoreViewHolder(
    view: View,
    private val listener: ItemPenaltyCalculationScoreAdapter.Listener
) : AbstractViewHolder<ItemPenaltyCalculationScoreUiModel>(view) {

    private val binding: ItemPenaltyCalculationScoreBinding? by viewBinding()

    override fun bind(element: ItemPenaltyCalculationScoreUiModel) {
        setupHeader(element.score, element.date)
        setupDetailRecyclerView(element.ongoingPoints, element.totalOrder, element.shopLevel)
    }

    private fun setupHeader(
        score: Int,
        date: String
    ) {
        binding?.tvPenaltyCalculationScoreValue?.text = score.toString()
        binding?.tvPenaltyCalculationScoreDate?.text = date
    }

    private fun setupDetailRecyclerView(
        ongoingPoints: Int,
        totalOrder: Int,
        shopLevel: Int
    ) {
        val adapterItems = getDetailAdapterItems(ongoingPoints, totalOrder, shopLevel)
        binding?.rvPenaltyCalculationScore?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ItemPenaltyCalculationScoreAdapter(
                adapterItems,
                listener
            )
        }
    }

    private fun getDetailAdapterItems(
        ongoingPoints: Int,
        totalOrder: Int,
        shopLevel: Int
    ): List<ItemPenaltyCalculationScoreDetailUiModel> {
        return listOf(
            ItemPenaltyCalculationScoreDetailUiModel(
                detail = getString(R.string.title_penalty_calculation_ongoing),
                value = ongoingPoints,
                shouldShowIcon = false
            ),
            ItemPenaltyCalculationScoreDetailUiModel(
                detail = getString(R.string.title_penalty_calculation_total_order),
                value = totalOrder,
                shouldShowIcon = false
            ),
            ItemPenaltyCalculationScoreDetailUiModel(
                detail = getString(R.string.title_penalty_calculation_shop_level),
                value = shopLevel,
                shouldShowIcon = true
            )
        )
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_calculation_score
    }

}

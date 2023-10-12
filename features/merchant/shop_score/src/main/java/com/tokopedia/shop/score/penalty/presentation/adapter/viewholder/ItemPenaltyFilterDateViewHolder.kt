package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyFilterDateBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyDateListener
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterDateUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyFilterDateViewHolder(
    view: View,
    private val filterPenaltyDateListener: FilterPenaltyDateListener?
): AbstractViewHolder<PenaltyFilterDateUiModel>(view) {

    private val binding: ItemPenaltyFilterDateBinding? by viewBinding()

    override fun bind(element: PenaltyFilterDateUiModel) {
        binding?.tvPenaltyFilterDate?.text = element.completeDate
        binding?.cardPenaltyFilterDate?.setOnClickListener {
            filterPenaltyDateListener?.onDatePicked(
                element.startDate,
                element.defaultStartDate,
                element.endDate,
                element.defaultEndDate
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_filter_date
    }

}

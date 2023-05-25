package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyPointCardBinding
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyPointCardViewHolder(view: View): AbstractViewHolder<ItemPenaltyPointCardUiModel>(view) {

    private val binding: ItemPenaltyPointCardBinding? by viewBinding()

    override fun bind(element: ItemPenaltyPointCardUiModel) {
        binding?.tvPenaltyCardPoint?.text = element.score.toString()
        binding?.tvPenaltyCardDate?.text = element.date
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_point_card
    }

}

package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltySubsectionBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltySubsectionListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltySubsectionUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltySubsectionViewHolder(view: View,
                                      private val listener: ItemPenaltySubsectionListener?): AbstractViewHolder<ItemPenaltySubsectionUiModel>(view) {

    private val binding: ItemPenaltySubsectionBinding? by viewBinding()

    override fun bind(element: ItemPenaltySubsectionUiModel) {
        binding?.tvPenaltySubsection?.text = element.sectionName
        binding?.tvPenaltySubsectionDate?.text = element.date
        binding?.icPenaltySubsection?.setOnClickListener {
            listener?.onIconClicked()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_subsection
    }

}

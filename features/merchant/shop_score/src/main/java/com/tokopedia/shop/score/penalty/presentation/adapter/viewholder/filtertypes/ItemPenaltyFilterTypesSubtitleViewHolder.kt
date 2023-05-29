package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.filtertypes

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyFilterTypeSubtitleBinding
import com.tokopedia.shop.score.penalty.presentation.model.filtertypes.ItemPenaltyFilterTypesSubtitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyFilterTypesSubtitleViewHolder(view: View): AbstractViewHolder<ItemPenaltyFilterTypesSubtitleUiModel>(view) {

    private val binding: ItemPenaltyFilterTypeSubtitleBinding? by viewBinding()

    override fun bind(element: ItemPenaltyFilterTypesSubtitleUiModel) {
        binding?.tvPenaltyFilterTypeSubtitle?.text = element.title
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_filter_type_subtitle
    }

}

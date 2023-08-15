package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltySubsectionBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltySubsectionListener
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltySubsectionUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltySubsectionViewHolder(view: View,
                                      private val listener: ItemPenaltySubsectionListener?): AbstractViewHolder<ItemPenaltySubsectionUiModel>(view) {

    private val binding: ItemPenaltySubsectionBinding? by viewBinding()

    override fun bind(element: ItemPenaltySubsectionUiModel) {
        binding?.tvPenaltySubsection?.text = getMessageFromPageType(element.pageType)
        binding?.tvPenaltySubsectionDate?.text = element.date
        binding?.icPenaltySubsection?.setOnClickListener {
            listener?.onPenaltySubsectionIconClicked()
        }
    }

    private fun getMessageFromPageType(pageType: String): String {
        val stringRes =
            when (pageType) {
                ShopPenaltyPageType.NOT_YET_DEDUCTED -> R.string.title_penalty_not_yet_deducted
                ShopPenaltyPageType.HISTORY -> R.string.title_penalty_done
                else -> R.string.title_penalty_ongoing
            }
        return getString(stringRes)
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_subsection
    }

}

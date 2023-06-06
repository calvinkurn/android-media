package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemEmptyStatePenaltyCustomBinding
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyEmptyStateUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyEmptyCustomViewHolder(view: View): AbstractViewHolder<ItemPenaltyEmptyStateUiModel>(view) {

    private val binding: ItemEmptyStatePenaltyCustomBinding? by viewBinding()

    override fun bind(element: ItemPenaltyEmptyStateUiModel) {
        binding?.emptyStatePenalty?.setTitle(getTitleMessage(element.pageType))
        binding?.emptyStatePenalty?.setDescription(getDescMessage(element.pageType))
        binding?.emptyStatePenalty?.setImageUrl(TokopediaImageUrl.IMG_SHOP_SCORE_PENALTY_EMPTY)
    }

    private fun getTitleMessage(@ShopPenaltyPageType pageType: String): String {
        val titleRes =
            when(pageType) {
                ShopPenaltyPageType.ONGOING -> R.string.title_empty_state_penalty_ongoing
                ShopPenaltyPageType.HISTORY -> R.string.title_empty_state_penalty_history
                else -> R.string.title_empty_state_penalty_not_yet_deducted
            }
        return getString(titleRes)
    }

    private fun getDescMessage(@ShopPenaltyPageType pageType: String): String {
        val titleRes =
            when(pageType) {
                ShopPenaltyPageType.ONGOING -> R.string.desc_empty_state_penalty_ongoing
                ShopPenaltyPageType.HISTORY -> R.string.desc_empty_state_penalty_history
                else -> R.string.desc_empty_state_penalty_not_yet_deducted
            }
        return getString(titleRes)
    }

    companion object {
        val LAYOUT = R.layout.item_empty_state_penalty_custom
    }

}

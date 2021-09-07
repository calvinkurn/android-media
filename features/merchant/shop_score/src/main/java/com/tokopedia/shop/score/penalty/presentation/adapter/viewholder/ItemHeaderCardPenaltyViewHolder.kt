package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.util.TypedValue
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.databinding.CardShopScoreTotalPenaltyBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemHeaderCardPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemCardShopPenaltyUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ItemHeaderCardPenaltyViewHolder(
    view: View,
    private val itemHeaderCardPenaltyListener: ItemHeaderCardPenaltyListener
) : AbstractViewHolder<ItemCardShopPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.card_shop_score_total_penalty
        const val ROUNDED_RADIUS = 8F
    }

    private val binding: CardShopScoreTotalPenaltyBinding? by viewBinding()

    override fun bind(element: ItemCardShopPenaltyUiModel?) {
        binding?.apply {
            itemHeaderCardPenaltyListener.impressLearnMorePenaltyPage()

            separatorMultipleDots.setBackgroundResource(R.drawable.ic_line_separator)

            tvContentPenalty.setTextMakeHyperlink(getString(R.string.content_penalty_label)) {
                itemHeaderCardPenaltyListener.onMoreInfoHelpPenaltyClicked()
            }
            bgTotalPenalty.loadImage(
                if (element?.hasPenalty == true) ShopScoreConstant.IC_HAS_PENALTY_URL
                else ShopScoreConstant.IC_NO_PENALTY_URL)

            bgTotalPenalty.shapeAppearanceModel = bgTotalPenalty.shapeAppearanceModel
                .toBuilder()
                .setAllCornerSizes(ROUNDED_RADIUS)
                .build()

            if (element?.hasPenalty == true) {
                tvCountTotalPenalty.text = element.totalPenalty.toString()
                root.context?.resources?.getDimension(R.dimen.scorePenaltyTextSize)
                    ?.let { tvCountTotalPenalty.setTextSize(TypedValue.COMPLEX_UNIT_PX, it) }
            } else {
                tvCountTotalPenalty.text = getString(R.string.desc_no_penalty)
                tvCountTotalPenalty.setType(Typography.HEADING_3)
            }
            tvTotalPointDeductions.text =
                if (element?.deductionPoints?.isLessThanZero() == true) element.deductionPoints.toString() else "-"
        }
    }
}
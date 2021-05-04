package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemHeaderCardPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemCardShopPenaltyUiModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.card_shop_score_total_penalty.view.*

class ItemHeaderCardPenaltyViewHolder(view: View,
                                      private val itemHeaderCardPenaltyListener: ItemHeaderCardPenaltyListener): AbstractViewHolder<ItemCardShopPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.card_shop_score_total_penalty
    }

    private val impressHolderLearnMore = ImpressHolder()

    override fun bind(element: ItemCardShopPenaltyUiModel?) {
        with(itemView) {
            tvContentPenalty?.addOnImpressionListener(impressHolderLearnMore) {
                itemHeaderCardPenaltyListener.impressLearnMorePenaltyPage()
            }
            tvContentPenalty?.setTextMakeHyperlink(getString(R.string.content_penalty_label)) {
                itemHeaderCardPenaltyListener.onMoreInfoHelpPenaltyClicked()
            }
            bgTotalPenalty?.background = context?.let {
                ContextCompat.getDrawable(it, if (element?.hasPenalty == true) R.drawable.ic_has_penalty else R.drawable.ic_no_penalty)
            }
            val roundedRadius = 8F
            bgTotalPenalty?.shapeAppearanceModel = bgTotalPenalty.shapeAppearanceModel
                    .toBuilder()
                    .setAllCornerSizes(roundedRadius)
                    .build()

            if (element?.hasPenalty == true) {
                tvCountTotalPenalty.text = element.totalPenalty.toString()
                context?.resources?.getDimension(R.dimen.scorePenaltyTextSize)?.let { tvCountTotalPenalty.setTextSize(TypedValue.COMPLEX_UNIT_PX, it) }
            } else {
                tvCountTotalPenalty?.text = getString(R.string.desc_no_penalty)
                tvCountTotalPenalty.setType(Typography.HEADING_3)
            }
            tvTotalPointDeductions?.text = if (element?.deductionPoints?.isLessThanZero() == true) element.deductionPoints.toString() else "-"
        }
    }

}
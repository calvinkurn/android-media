package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemCardShopPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemCardShopPenaltyUiModel
import kotlinx.android.synthetic.main.card_shop_score_total_penalty.view.*

class ItemCardShopPenaltyViewHolder(view: View,
                                    private val itemCardShopPenaltyListener: ItemCardShopPenaltyListener) : AbstractViewHolder<ItemCardShopPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.card_shop_score_total_penalty
    }

    override fun bind(element: ItemCardShopPenaltyUiModel?) {
        if (element == null) return

        with(itemView) {
            tvContentPenalty?.setTextMakeHyperlink(getString(R.string.content_penalty_label)) {
                itemCardShopPenaltyListener.onMoreInfoHelpPenaltyClicked()
            }
            bgTotalPenalty?.background = ContextCompat.getDrawable(context, if
                    (element.hasPenalty) R.drawable.ic_has_penalty else R.drawable.ic_no_penalty)
            val roundedRadius = 8F
            bgTotalPenalty?.shapeAppearanceModel = bgTotalPenalty.shapeAppearanceModel
                    .toBuilder()
                    .setAllCornerSizes(roundedRadius)
                    .build()

            if (element.totalPenalty.isZero()) {
                tvCountTotalPenalty?.text = getString(R.string.desc_no_penalty)
            } else {
                tvCountTotalPenalty.text = element.totalPenalty.toString()
                tvCountTotalPenalty.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.scorePenaltyTextSize))
            }
            tvTotalPointDeductions?.text = if (element.deductionPoints.isLessThanZero()) element.deductionPoints.toString() else "-"
        }
    }

}
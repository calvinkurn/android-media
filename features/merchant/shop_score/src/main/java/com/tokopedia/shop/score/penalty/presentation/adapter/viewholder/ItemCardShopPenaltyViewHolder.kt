package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.BG_HAS_PENALTY
import com.tokopedia.shop.score.common.ShopScoreConstant.BG_NO_PENALTY
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.penalty.presentation.model.ItemCardShopPenaltyUiModel
import kotlinx.android.synthetic.main.card_shop_score_total_penalty.view.*

class ItemCardShopPenaltyViewHolder(view: View): AbstractViewHolder<ItemCardShopPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.card_shop_score_total_penalty
    }

    override fun bind(element: ItemCardShopPenaltyUiModel?) {
        if (element == null) return

        with(itemView) {
            setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            tvContentPenalty?.setTextMakeHyperlink(getString(R.string.content_penalty_label)) {
                //TODO
            }
            bgTotalPenalty?.loadImage(if (element.hasPenalty) BG_HAS_PENALTY else BG_NO_PENALTY)
            tvCountTotalPenalty?.text = if (element.totalPenalty.isZero()) getString(R.string.desc_no_penalty)
                        else element.totalPenalty.toString()
            tvTotalPointDeductions?.text = if (element.deductionPoints.isLessThanZero()) element.deductionPoints.toString() else "-"
        }
    }

}
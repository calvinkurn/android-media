package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.EMPTY_STATE_PENALTY_URL
import com.tokopedia.unifycomponents.setHeadingText
import kotlinx.android.synthetic.main.item_empty_state_penalty.view.*

class ItemPenaltyEmptyViewHolder(view: View): AbstractViewHolder<EmptyModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_empty_state_penalty
    }

    override fun bind(element: EmptyModel?) {
        with(itemView) {
            emptyStatePenalty?.setImageUrl(EMPTY_STATE_PENALTY_URL)
        }
    }
}
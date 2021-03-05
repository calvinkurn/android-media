package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.TransitionPeriodReliefUiModel
import kotlinx.android.synthetic.main.item_card_transition_period_relief.view.*

class TransitionPeriodReliefViewHolder(view: View):
        AbstractViewHolder<TransitionPeriodReliefUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_card_transition_period_relief
    }

    override fun bind(element: TransitionPeriodReliefUiModel?) {
        with(itemView) {
            tvDescSellerTransitionPeriod.text = getString(R.string.title_transition_period_relief, element?.dateTransitionPeriodRelief)
        }
    }
}
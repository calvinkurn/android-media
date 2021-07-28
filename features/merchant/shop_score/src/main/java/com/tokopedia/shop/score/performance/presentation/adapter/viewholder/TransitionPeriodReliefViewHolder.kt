package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.TransitionPeriodReliefUiModel
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.item_card_transition_period_relief.view.*

class TransitionPeriodReliefViewHolder(view: View):
        AbstractViewHolder<TransitionPeriodReliefUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_card_transition_period_relief
    }

    override fun bind(element: TransitionPeriodReliefUiModel?) {
        with(itemView) {
            icSellerTransitionPeriod?.loadImage(element?.iconTransitionPeriodRelief.orEmpty())
            tvDescSellerTransitionPeriod?.text = getString(R.string.desc_transition_period_relief, element?.dateTransitionPeriodRelief)
        }
        setupDarkModeColor()
    }

    private fun setupDarkModeColor() {
        with(itemView) {
            if(context.isDarkMode()) {
                cardTransitionPeriodRelief?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN300))
            }
        }
    }
}
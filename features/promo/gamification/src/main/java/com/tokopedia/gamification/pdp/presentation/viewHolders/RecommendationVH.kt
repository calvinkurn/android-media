package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.R
import com.tokopedia.gamification.pdp.data.Recommendation

class RecommendationVH(itemView: View) : AbstractViewHolder<Visitable<Recommendation>>(itemView) {

    override fun bind(element: Visitable<Recommendation>?) {

    }

    companion object {
        val LAYOUT = R.layout.item_recommendation_pdp_gami
    }
}
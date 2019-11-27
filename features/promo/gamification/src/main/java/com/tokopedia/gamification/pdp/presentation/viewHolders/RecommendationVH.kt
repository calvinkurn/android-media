package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.R
import com.tokopedia.gamification.pdp.data.Recommendation

class RecommendationVH(itemView: View) : AbstractViewHolder<Recommendation>(itemView) {

    override fun bind(element: Recommendation?) {

    }

    companion object {
        val LAYOUT = R.layout.item_recommendation_pdp_gami
    }
}
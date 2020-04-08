package com.tokopedia.gamification.pdp.presentation.adapters

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.presentation.GamiPdpRecommendationListener
import com.tokopedia.gamification.pdp.presentation.viewHolders.RecommendationVH

class PdpGamificationAdapterTypeFactory(val recommendationListener: GamiPdpRecommendationListener) : BaseAdapterTypeFactory(), PdpGamificationTypeFactory{

    override fun type(recomendation: Recommendation): Int {
        return RecommendationVH.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            RecommendationVH.LAYOUT->RecommendationVH(parent, recommendationListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
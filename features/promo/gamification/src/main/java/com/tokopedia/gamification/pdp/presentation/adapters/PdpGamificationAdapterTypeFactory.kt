package com.tokopedia.gamification.pdp.presentation.adapters

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.presentation.viewHolders.RecommendationVH
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class PdpGamificationAdapterTypeFactory(val recommendationListener: RecommendationListener) : BaseAdapterTypeFactory(), PdpGamificationTypeFactory{

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
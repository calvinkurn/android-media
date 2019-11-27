package com.tokopedia.gamification.pdp.presentation.adapters

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.presentation.viewHolders.RecommendationVH

class PdpGamificationAdapterTypeFactory : BaseAdapterTypeFactory(), PdpGamificationTypeFactory{

    override fun type(recomendation: Recommendation): Int {
        return RecommendationVH.LAYOUT
    }

}
package com.tokopedia.gamification.pdp.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationAdapterTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class Recommendation(val recommendationItem: RecommendationItem) : Visitable<PdpGamificationAdapterTypeFactory> {
    override fun type(typeFactory: PdpGamificationAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
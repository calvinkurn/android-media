package com.tokopedia.gamification.pdp.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class Recommendation(val recommendationItem: RecommendationItem) : Visitable<PdpGamificationTypeFactory> {

    override fun type(typeFactory: PdpGamificationTypeFactory): Int {
        return typeFactory.type(this)
    }
}

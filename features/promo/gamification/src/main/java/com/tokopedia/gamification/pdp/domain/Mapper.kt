package com.tokopedia.gamification.pdp.domain

import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import java.util.*

class Mapper {

    fun recommWidgetToListOfVisitables(recommendationWidget: RecommendationWidget): List<Recommendation> {
        val recomendationList = ArrayList<Recommendation>()
        for (item in recommendationWidget.recommendationItemList) {
            recomendationList.add(Recommendation(item))
        }
        return recomendationList
    }
}
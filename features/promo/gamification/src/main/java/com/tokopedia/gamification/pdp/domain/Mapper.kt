package com.tokopedia.gamification.pdp.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import java.util.*

class Mapper {

//    fun <T,V> map(t:T):V
    fun recommWidgetToListOfVisitables(recommendationWidget: RecommendationWidget): List<Visitable<*>>{
    val recomendationList = ArrayList<Visitable<*>>()
    for (item in recommendationWidget.recommendationItemList) {
        recomendationList.add(Recommendation(item))
    }
    return recomendationList
    }
}
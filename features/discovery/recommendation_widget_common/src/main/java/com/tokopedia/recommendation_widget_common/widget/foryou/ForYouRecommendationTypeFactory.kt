package com.tokopedia.recommendation_widget_common.widget.foryou

import com.tokopedia.recommendation_widget_common.widget.foryou.recomcard.RecomEntityModel

interface ForYouRecommendationTypeFactory {

    fun type(model: RecomEntityModel): Int
}

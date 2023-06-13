package com.tokopedia.recommendation_widget_common.widget.vertical

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.recommendation_widget_common.widget.vertical.model.RecommendationVerticalProductCardModel
import com.tokopedia.recommendation_widget_common.widget.vertical.model.RecommendationVerticalSeeMoreModel

interface RecommendationVerticalTypeFactory: AdapterTypeFactory {
    fun type(recommendationVerticalProductCardModel: RecommendationVerticalProductCardModel): Int
    fun type(recommendationVerticalProductCardModel: RecommendationVerticalSeeMoreModel): Int
}

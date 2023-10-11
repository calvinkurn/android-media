package com.tokopedia.recommendation_widget_common.widget.vertical

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory

interface RecommendationVerticalTypeFactory: AdapterTypeFactory {
    fun type(recommendationVerticalProductCardModel: RecommendationVerticalProductCardModel): Int
    fun type(recommendationVerticalProductCardModel: RecommendationVerticalSeeMoreModel): Int
}

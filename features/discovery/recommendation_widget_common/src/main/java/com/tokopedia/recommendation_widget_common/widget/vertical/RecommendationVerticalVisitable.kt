package com.tokopedia.recommendation_widget_common.widget.vertical

import com.tokopedia.abstraction.base.view.adapter.Visitable

interface RecommendationVerticalVisitable : Visitable<RecommendationVerticalTypeFactory> {
    fun areItemsTheSame(toCompare: RecommendationVerticalVisitable): Boolean

    fun areContentsTheSame(toCompare: RecommendationVerticalVisitable): Boolean

    fun getChangePayload(toCompare: RecommendationVerticalVisitable): Map<String, Any>
}

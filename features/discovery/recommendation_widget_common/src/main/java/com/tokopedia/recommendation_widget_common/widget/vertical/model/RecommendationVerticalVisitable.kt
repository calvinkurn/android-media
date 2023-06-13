package com.tokopedia.recommendation_widget_common.widget.vertical.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalTypeFactory

interface RecommendationVerticalVisitable : Visitable<RecommendationVerticalTypeFactory> {
    fun areItemsTheSame(toCompare: RecommendationVerticalVisitable): Boolean

    fun areContentsTheSame(toCompare: RecommendationVerticalVisitable): Boolean

    fun getChangePayload(toCompare: RecommendationVerticalVisitable): Map<String, Any>
}

package com.tokopedia.recommendation_widget_common.widget.foryou.state.model

import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationVisitable

data class EmptyStateModel(
    val id: String = ID
) : ForYouRecommendationVisitable {

    override fun areContentsTheSame(other: Any) =
        other is ShimmeringStateModel && other.id == id

    override fun areItemsTheSame(other: Any) = true

    override fun type(typeFactory: ForYouRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        private const val ID = "RECOMMENDATION_EMPTY"
    }
}

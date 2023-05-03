package com.tokopedia.recommendation_widget_common.widget.comparison_bpc

import com.tokopedia.recommendation_widget_common.widget.global.*

/**
 * Created by frenzel on 27/03/23
 */
data class RecommendationComparisonBpcModel(
    val recommendationVisitable: RecommendationVisitable
) : RecommendationVisitable by recommendationVisitable {
    override fun type(typeFactory: RecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}

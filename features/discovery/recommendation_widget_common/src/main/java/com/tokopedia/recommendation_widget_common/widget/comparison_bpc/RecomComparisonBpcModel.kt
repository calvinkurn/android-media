package com.tokopedia.recommendation_widget_common.widget.comparison_bpc

import com.tokopedia.recommendation_widget_common.widget.global.*

/**
 * Created by frenzel on 27/03/23
 */
data class RecomComparisonBpcModel(
    val recomVisitable: RecomVisitable
) : RecomVisitable by recomVisitable {
    override fun type(typeFactory: RecomTypeFactory): Int {
        return typeFactory.type(this)
    }
}

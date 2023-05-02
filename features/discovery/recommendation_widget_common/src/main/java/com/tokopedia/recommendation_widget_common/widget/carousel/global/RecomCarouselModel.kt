package com.tokopedia.recommendation_widget_common.widget.carousel.global

import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselChipListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowListener
import com.tokopedia.recommendation_widget_common.widget.global.RecomTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecomVisitable

/**
 * Created by frenzel on 27/03/23
 */
data class RecomCarouselModel(
    val recomVisitable: RecomVisitable,
    val tokonowListener: RecommendationCarouselTokonowListener? = null,
    val chipListener: RecomCarouselChipListener? = null
) : RecomVisitable by recomVisitable {
    override fun type(typeFactory: RecomTypeFactory): Int {
        return typeFactory.type(this)
    }

    val isTokonow: Boolean = this.recomWidgetMetadata.pageName == PAGE_NAME_TOKONOW

    companion object {
        private const val PAGE_NAME_TOKONOW = "pdp_9_tokonow"
    }
}

package com.tokopedia.tokopedianow.common.model

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_LOADING
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory
import com.tokopedia.tokopedianow.searchcategory.utils.OOC_TOKONOW

class TokoNowRecommendationCarouselUiModel(
        val id: String = "",
        val pageName: String,
        var carouselData: RecommendationCarouselData = RecommendationCarouselData(state = STATE_LOADING),
): TokoNowLayoutUiModel(id) {

    override fun type(typeFactory: TokoNowTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    fun isOutOfCoverage() = pageName == OOC_TOKONOW
}
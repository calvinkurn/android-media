package com.tokopedia.tokopedianow.common.model

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.OOC_TOKONOW
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_LOADING
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory

class TokoNowRecommendationCarouselUiModel(
    val id: String = "",
    val pageName: String,
    var carouselData: RecommendationCarouselData = RecommendationCarouselData(state = STATE_LOADING),
    var isBindWithPageName: Boolean = false,
    var keywords: String = "",
    var categoryId: List<String> = listOf(),
    var isFirstLoad: Boolean = true

): TokoNowLayoutUiModel(id) {

    override fun type(typeFactory: TokoNowTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    fun isOutOfCoverage() = pageName == OOC_TOKONOW
}
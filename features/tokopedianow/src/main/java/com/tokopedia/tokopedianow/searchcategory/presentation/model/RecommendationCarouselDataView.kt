package com.tokopedia.tokopedianow.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_LOADING
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory
import com.tokopedia.tokopedianow.searchcategory.utils.OOC_TOKONOW

class RecommendationCarouselDataView(
        val pageName: String,
        var carouselData: RecommendationCarouselData = RecommendationCarouselData(state = STATE_LOADING),
): Visitable<BaseSearchCategoryTypeFactory> {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    fun isOutOfCoverage() = pageName == OOC_TOKONOW
}
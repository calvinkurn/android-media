package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.OOC_TOKONOW
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_LOADING
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationOocTypeFactory

class TokoNowProductRecommendationOocUiModel(
    val id: String = "",
    val pageName: String,
    var carouselData: RecommendationCarouselData = RecommendationCarouselData(state = STATE_LOADING),
    var isBindWithPageName: Boolean = false,
    var keywords: String = "",
    var categoryId: List<String> = listOf(),
    var isFirstLoad: Boolean = true,
    var miniCartSource: MiniCartSource = MiniCartSource.TokonowRecommendationPage,
    var isTokoNow: Boolean = true
): Visitable<TokoNowProductRecommendationOocTypeFactory> {

    override fun type(typeFactory: TokoNowProductRecommendationOocTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    fun isOutOfCoverage() = pageName == OOC_TOKONOW
}

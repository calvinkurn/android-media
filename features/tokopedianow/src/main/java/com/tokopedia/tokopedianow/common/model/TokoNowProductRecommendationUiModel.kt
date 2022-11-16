package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationTypeFactory

class TokoNowProductRecommendationUiModel(
    val id: String = "",
    val pageName: String,
    var miniCartSource: MiniCartSource = MiniCartSource.TokonowRecommendationPage,
    var requestParam: GetRecommendationRequestParam
): Visitable<TokoNowProductRecommendationTypeFactory> {
    override fun type(typeFactory: TokoNowProductRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}

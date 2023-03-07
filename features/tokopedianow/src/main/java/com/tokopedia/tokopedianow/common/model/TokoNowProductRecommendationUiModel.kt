package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationTypeFactory

data class TokoNowProductRecommendationUiModel(
    val requestParam: GetRecommendationRequestParam
): Visitable<TokoNowProductRecommendationTypeFactory> {
    override fun type(typeFactory: TokoNowProductRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}

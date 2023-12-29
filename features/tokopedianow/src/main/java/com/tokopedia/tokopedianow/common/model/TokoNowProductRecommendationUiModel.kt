package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationTypeFactory

data class TokoNowProductRecommendationUiModel(
    val id: String = String.EMPTY,
    val requestParam: GetRecommendationRequestParam,
    val tickerPageSource: String = String.EMPTY
): Visitable<TokoNowProductRecommendationTypeFactory> {
    override fun type(typeFactory: TokoNowProductRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}

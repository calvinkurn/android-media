package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

class HomeProductRecomUiModel(
    val id: String,
    val recomWidget: RecommendationWidget
) : HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
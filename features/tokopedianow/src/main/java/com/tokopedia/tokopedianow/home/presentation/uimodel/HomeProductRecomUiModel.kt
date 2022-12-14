package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeProductRecomUiModel(
    val id: String,
    val recomWidget: RecommendationWidget,
    val realTimeRecom: HomeRealTimeRecomUiModel = HomeRealTimeRecomUiModel()
) : HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getChangePayload(newModel: HomeLayoutUiModel): Any? {
        val newItem = newModel as HomeProductRecomUiModel
        val oldRecomWidget = recomWidget.recommendationItemList
        val newRecomWidget = newItem.recomWidget.recommendationItemList
        val oldRtrWidget = realTimeRecom
        val newRtrWidget = newItem.realTimeRecom

        return when {
            oldRecomWidget != newRecomWidget ||
            oldRtrWidget != newRtrWidget -> true
            else -> null
        }
    }
}

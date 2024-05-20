package com.tokopedia.recommendation_widget_common.widget.vertical

import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

object RecommendationVerticalMapper {

    fun mapVisitableList(model: RecommendationVerticalModel): List<RecommendationVerticalVisitable> {
        return mutableListOf<RecommendationVerticalVisitable>().apply {
            addAll(mapRecommendationVerticalProductCard(model))
            if (model.widget.seeMoreAppLink.isNotEmpty()) {
                add(mapRecommendationVerticalSeeMore(model))
            }
        }
    }

    private fun mapRecommendationVerticalProductCard(
        model: RecommendationVerticalModel
    ): List<RecommendationVerticalVisitable> {
        return model.widget.recommendationItemList.map {
            RecommendationVerticalProductCardModel(
                productModel = it.toProductCardModel(),
                recomItem = it,
                recomWidget = model.widget,
                trackingModel = model.trackingModel,
                componentName = model.widget.pageName,
                widgetTracking = model.widgetTracking,
                source = model.source,
                appLogAdditionalParam = model.appLogAdditionalParam,
            )
        }
    }

    private fun mapRecommendationVerticalSeeMore(
        model: RecommendationVerticalModel
    ): RecommendationVerticalVisitable {
        return RecommendationVerticalSeeMoreModel(
            appLink = model.widget.seeMoreAppLink,
            recomWidget = model.widget,
            componentName = model.widget.pageName,
            widgetTracking = model.widgetTracking,
        )
    }
}

package com.tokopedia.recommendation_widget_common.widget.vertical

import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

object RecommendationVerticalMapper {

    fun mapVisitableList(model: RecommendationVerticalModel): List<RecommendationVerticalVisitable> {
        return mutableListOf<RecommendationVerticalVisitable>().apply {
            addAll(mapRecommendationVerticalProductCard(model))
            if (model.widget.hasNext) {
                add(mapRecommendationVerticalSeeMore(model.widget))
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
                componentName = model.widget.pageName
            )
        }
    }

    private fun mapRecommendationVerticalSeeMore(
        widget: RecommendationWidget
    ): RecommendationVerticalVisitable {
        return RecommendationVerticalSeeMoreModel(
            appLink = widget.seeMoreAppLink,
            recomWidget = widget,
            componentName = widget.pageName
        )
    }
}

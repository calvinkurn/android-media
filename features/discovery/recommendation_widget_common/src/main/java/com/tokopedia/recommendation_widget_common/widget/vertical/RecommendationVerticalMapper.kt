package com.tokopedia.recommendation_widget_common.widget.vertical

import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.vertical.model.RecommendationVerticalProductCardModel
import com.tokopedia.recommendation_widget_common.widget.vertical.model.RecommendationVerticalSeeMoreModel
import com.tokopedia.recommendation_widget_common.widget.vertical.model.RecommendationVerticalVisitable

object RecommendationVerticalMapper {
    fun mapToAnnotateChip(
        recommendationFilterChips: List<RecommendationFilterChipsEntity.RecommendationFilterChip>,
        selectedRecommendationFilterChips: RecommendationFilterChipsEntity.RecommendationFilterChip?
    ): List<AnnotationChip> {
        return recommendationFilterChips.map {
            AnnotationChip(
                recommendationFilterChip = if (it.name == selectedRecommendationFilterChips?.name) {
                    it.copy(isActivated = true)
                } else it
            )
        }
    }

    fun mapVisitableList(
        widget: RecommendationWidget
    ): List<RecommendationVerticalVisitable> {
        return mutableListOf<RecommendationVerticalVisitable>().apply {
            addAll(mapRecommendationVerticalProductCard(widget))
            if (widget.hasNext) {
                add(mapRecommendationVerticalSeeMore(widget))
            }
        }
    }

    private fun mapRecommendationVerticalProductCard(
        widget: RecommendationWidget
    ): List<RecommendationVerticalVisitable> {
        return widget.recommendationItemList.map {
            RecommendationVerticalProductCardModel(
                productModel = it.toProductCardModel(),
                recomItem = it,
                recomWidget = widget,
                componentName = widget.pageName
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

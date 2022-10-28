package com.tokopedia.product.detail.view.util

import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip

object ProductRecommendationMapper {

    fun selectOrDeselectAnnotationChip(filterData: List<AnnotationChip>?, name: String, isActivated: Boolean): List<AnnotationChip> {
        return filterData?.map {
            it.copy(
                    recommendationFilterChip = it.recommendationFilterChip.copy(
                            isActivated =
                            name == it.recommendationFilterChip.name
                                    && isActivated
                    )
            )
        } ?: listOf()
    }
}
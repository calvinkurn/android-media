package com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter

import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity

/**
 * Created by Lukas on 05/11/20.
 */
interface AnnotationChipListener{

    fun onFilterAnnotationImpressed(
        annotationChip: RecommendationFilterChipsEntity.RecommendationFilterChip,
    )
    fun onFilterAnnotationClicked(annotationChip: RecommendationFilterChipsEntity.RecommendationFilterChip, position: Int)
}

package com.tokopedia.recommendation_widget_common.widget.productcard.common

interface RecomCarouselDiffUtilComparable {

    fun areItemsTheSame(toCompare: RecomCarouselDiffUtilComparable): Boolean

    fun areContentsTheSame(toCompare: RecomCarouselDiffUtilComparable): Boolean
}
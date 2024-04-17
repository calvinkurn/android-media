package com.tokopedia.recommendation_widget_common.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface AdsViewListener {
    fun onViewAttachedToWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int)
    fun onViewDetachedFromWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int, visiblePercentage: Int)
}

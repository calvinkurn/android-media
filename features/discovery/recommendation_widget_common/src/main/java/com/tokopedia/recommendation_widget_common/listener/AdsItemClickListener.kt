package com.tokopedia.recommendation_widget_common.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface AdsItemClickListener {
    fun onAreaClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {}
    fun onProductImageClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {}
    fun onSellerInfoClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {}
}

package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface RecommendationWidgetListener {

    fun onViewAttachedToWindow(position: Int, item: RecommendationItem) {}

    fun onViewDetachedFromWindow(position: Int, item: RecommendationItem, visiblePercentage: Int) {}

    fun onProductClick(position: Int, item: RecommendationItem): Boolean = false

    fun onAreaClicked(position: Int, item: RecommendationItem) {}

    fun onProductImageClicked(position: Int, item: RecommendationItem) {}

    fun onSellerInfoClicked(position: Int, item: RecommendationItem) {}

    fun onProductImpress(position: Int, item: RecommendationItem): Boolean = false

    fun onProductAddToCartClick(item: RecommendationItem): Boolean = false

    fun onProductAddToCartSuccess(item: RecommendationItem, addToCartData: AddToCartDataModel): Boolean = false

    fun onProductAddToCartFailed(): Boolean = false
}

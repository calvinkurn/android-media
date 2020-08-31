package com.tokopedia.thankyou_native.recommendation.presentation.adapter.listener

import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface MarketPlaceRecommendationViewListener : RecommendationListener {
    fun onProductImpression(item: RecommendationItem, position: Int)
    fun onProductAddToCartClick(item: RecommendationItem, position: Int)
    fun onWishListedSuccessfully(message: String)
    fun onRemoveFromWishList(message: String)
    fun onShowError(throwable: Throwable?)
    fun onRecommendationItemDisplayed(recommendationItem: RecommendationItem, position: Int)
}

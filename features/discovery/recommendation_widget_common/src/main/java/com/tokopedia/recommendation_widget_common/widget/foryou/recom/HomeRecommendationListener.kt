package com.tokopedia.recommendation_widget_common.widget.foryou.recom

interface HomeRecommendationListener {
    fun onProductImpression(model: HomeRecommendationModel, position: Int)
    fun onProductClick(model: HomeRecommendationModel, position: Int)
    fun onProductThreeDotsClick(model: HomeRecommendationModel, position: Int)
}

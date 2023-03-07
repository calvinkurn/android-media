package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

sealed class ViewToViewRecommendationResult {
    data class Loading(
        val hasATCButton: Boolean
    ) : ViewToViewRecommendationResult()

    data class Product(
        val products: List<ViewToViewDataModel> = emptyList(),
    ) : ViewToViewRecommendationResult()
}

package com.tokopedia.tokopedianow.common.analytics

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface RealTimeRecommendationAnalytics {

    fun trackWidgetImpression(productId: String, warehouseId: String)

    fun trackRefreshImpression(productId: String)

    fun trackClickRefresh(productId: String)

    fun trackClickClose(productId: String)

    fun trackProductImpression(
        headerName: String,
        productId: String,
        recomItem: RecommendationItem,
        position: Int
    )

    fun trackProductClick(
        headerName: String,
        productId: String,
        recomItem: RecommendationItem,
        position: Int
    )

    fun trackAddToCart(
        productId: String,
        recomItem: RecommendationItem,
        quantity: Int
    )
}

package com.tokopedia.tokopedianow.common.analytics

import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel

interface RealTimeRecommendationAnalytics {

    fun trackWidgetImpression(productId: String, warehouseId: String)

    fun trackRefreshImpression(productId: String)

    fun trackClickRefresh(productId: String)

    fun trackClickClose(productId: String)

    fun trackProductImpression(
        headerName: String,
        productId: String,
        item: ProductCardCompactCarouselItemUiModel,
        position: Int
    )

    fun trackProductClick(
        headerName: String,
        productId: String,
        item: ProductCardCompactCarouselItemUiModel,
        position: Int
    )

    fun trackAddToCart(
        productId: String,
        item: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    )
}

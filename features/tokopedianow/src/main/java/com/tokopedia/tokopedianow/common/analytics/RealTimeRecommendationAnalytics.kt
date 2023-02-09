package com.tokopedia.tokopedianow.common.analytics

import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel

interface RealTimeRecommendationAnalytics {

    fun trackWidgetImpression(productId: String, warehouseId: String)

    fun trackRefreshImpression(productId: String)

    fun trackClickRefresh(productId: String)

    fun trackClickClose(productId: String)

    fun trackProductImpression(
        headerName: String,
        productId: String,
        item: TokoNowProductCardCarouselItemUiModel,
        position: Int
    )

    fun trackProductClick(
        headerName: String,
        productId: String,
        item: TokoNowProductCardCarouselItemUiModel,
        position: Int
    )

    fun trackAddToCart(
        productId: String,
        item: TokoNowProductCardCarouselItemUiModel,
        quantity: Int
    )
}

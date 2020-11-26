package com.tokopedia.product.manage.feature.list.view.model

sealed class TopAdsPage(open val productId: String) {
    data class OnBoarding(override val productId: String): TopAdsPage(productId)
    data class ManualAds(override val productId: String): TopAdsPage(productId)
    data class AutoAds(override val productId: String): TopAdsPage(productId)
}
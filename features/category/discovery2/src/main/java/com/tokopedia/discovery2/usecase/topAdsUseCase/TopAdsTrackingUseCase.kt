package com.tokopedia.discovery2.usecase.topAdsUseCase

abstract class TopAdsTrackingUseCase {
    abstract fun hitImpressions(className: String?, url: String, productId: String, productName: String, imageUrl: String)
    abstract fun hitClick(className: String?, url: String, productId: String, productName: String, imageUrl: String)
}
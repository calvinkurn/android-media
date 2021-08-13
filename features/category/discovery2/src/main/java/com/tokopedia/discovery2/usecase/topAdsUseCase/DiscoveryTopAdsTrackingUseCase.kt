package com.tokopedia.discovery2.usecase.topAdsUseCase

import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import javax.inject.Inject

class DiscoveryTopAdsTrackingUseCase @Inject constructor(val topAdsUrlHitter: TopAdsUrlHitter) : TopAdsTrackingUseCase(){

    override fun hitImpressions(className: String?, url: String, productId: String, productName: String, imageUrl: String) {
        if (!className.isNullOrEmpty() && url.isNotEmpty()) {
            topAdsUrlHitter.hitImpressionUrl(className, url, productId, productName, imageUrl)
        }
    }

    override fun hitClick(className: String?, url: String, productId: String, productName: String, imageUrl: String) {
        if (!className.isNullOrEmpty() && url.isNotEmpty()) {
            topAdsUrlHitter.hitClickUrl(className, url, productId, productName, imageUrl)
        }
    }
}
package com.tokopedia.feedcomponent.analytics.topadstracker

import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import javax.inject.Inject

class SendTopAdsUseCase @Inject constructor(private val topAdsUrlHitter: TopAdsUrlHitter) {

    private var className = SendTopAdsUseCase::class.java.name

    fun hitImpressions(url: String, productId: String, productName: String, imageUrl: String) {
        topAdsUrlHitter.hitImpressionUrl(className, url, productId, productName, imageUrl)
    }

    fun hitClick(url: String, productId: String, productName: String, imageUrl: String) {
        topAdsUrlHitter.hitClickUrl(className, url, productId, productName, imageUrl)
    }
}
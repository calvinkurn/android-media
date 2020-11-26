package com.tokopedia.common_category.usecase

import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import javax.inject.Inject

class SendTopAdsUseCase @Inject constructor(val topAdsUrlHitter: TopAdsUrlHitter) {

    companion object {
        private const val className: String = "com.tokopedia.common_category.usecase.SendTopAdsUseCase";
    }

    fun hitImpressions(url: String, productId: String, productName: String, imageUrl: String) {
        topAdsUrlHitter.hitImpressionUrl(className, url, productId, productName, imageUrl)
    }

    fun hitClick(url: String, productId: String, productName: String, imageUrl: String) {
        topAdsUrlHitter.hitClickUrl(className, url, productId, productName, imageUrl)
    }
}
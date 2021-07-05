package com.tokopedia.categorylevels.domain.usecase

import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import javax.inject.Inject

class CategoryTopAdsTrackingUseCase @Inject constructor(val topAdsUrlHitter: TopAdsUrlHitter)  : TopAdsTrackingUseCase() {

    companion object {
        private const val categoryClassName: String = "category_levels_top";
    }
    override fun hitImpressions(className: String?, url: String, productId: String, productName: String, imageUrl: String) {
        topAdsUrlHitter.hitImpressionUrl(categoryClassName, url, productId, productName, imageUrl)
    }

    override fun hitClick(className: String?, url: String, productId: String, productName: String, imageUrl: String) {
        topAdsUrlHitter.hitClickUrl(categoryClassName, url, productId, productName, imageUrl)
    }
}
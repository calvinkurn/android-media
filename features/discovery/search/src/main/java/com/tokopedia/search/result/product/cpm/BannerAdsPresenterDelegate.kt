package com.tokopedia.search.result.product.cpm

import com.tokopedia.topads.sdk.utils.TopAdsHeadlineHelper

class BannerAdsPresenterDelegate constructor(
    private val topAdsHeadlineHelper : TopAdsHeadlineHelper
): BannerAdsPresenter {
    override fun shopAdsImpressionCount(impressionCount: Int) {
        topAdsHeadlineHelper.seenAds = impressionCount
    }
}
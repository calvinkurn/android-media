package com.tokopedia.topads.sdk.utils

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.sdk.domain.TopAdsParams
import java.util.HashMap

private const val HEADLINE = "headline"
private const val HEADLINE_TEMPLATE_VALUE = "3,4"
private const val HEADLINE_PRODUCT_COUNT = 3
private const val INFINITESEARCH = "infinitesearch"
private const val SEEN_ADS = "seen_ads"

object TopAdsHeadlineViewParams {
    fun createHeadlineParams(
            parameters: Map<String, Any?>?,
            itemCount: Int,
            impressionCount: String
    ): String {
        val headlineParams = HashMap(parameters)

        headlineParams[TopAdsParams.KEY_EP] = HEADLINE
        headlineParams[TopAdsParams.KEY_TEMPLATE_ID] = HEADLINE_TEMPLATE_VALUE
        headlineParams[TopAdsParams.KEY_ITEM] = itemCount
        headlineParams[TopAdsParams.KEY_HEADLINE_PRODUCT_COUNT] = HEADLINE_PRODUCT_COUNT
        headlineParams[INFINITESEARCH] = true
        if (impressionCount.toIntOrZero() != 0) headlineParams[SEEN_ADS] = impressionCount

        return UrlParamHelper.generateUrlParamString(headlineParams)
    }
}

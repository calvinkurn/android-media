package com.tokopedia.macrobenchmark_util.env.mock.config

import android.content.Context
import android.content.Intent
import com.tokopedia.macrobenchmark_util.env.mock.MockModelConfig

class SearchMockResponseConfig(): MockModelConfig() {
    companion object {
        const val KEY_QUERY_SEARCH_PRODUCT = "SearchProduct"
        const val KEY_QUERY_KERO = "KeroAddrGetStateChosenAddress"
        const val KEY_QUERY_TOPADS_BANNER = "TopAdsBannerQuery"
        const val KEY_QUERY_HEADLINE_ADS = "HeadlineAds"
    }
    override fun createMockModel(context: Context, intent: Intent): MockModelConfig {
        val mapMockResponse = createMapOfMockResponse(context, intent)

        for ((key, value) in mapMockResponse.entries) addMockResponse(key, value?:"", FIND_BY_CONTAINS)

        return this
    }

    private fun createMapOfMockResponse(context: Context, intent: Intent) = mapOf(
            KEY_QUERY_SEARCH_PRODUCT to intent.getStringExtra("SearchProduct"),
            KEY_QUERY_KERO to intent.getStringExtra("KeroAddrGetStateChosenAddress"),
            KEY_QUERY_TOPADS_BANNER to intent.getStringExtra("TopAdsBannerQuery"),
            KEY_QUERY_HEADLINE_ADS to intent.getStringExtra("HeadlineAds")
    )
}
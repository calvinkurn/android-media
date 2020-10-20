package com.tokopedia.topads.sdk

import com.tokopedia.url.TokopediaUrl

object UrlTopAdsSdk {

    var BASE_URL = TokopediaUrl.getInstance().TA

    private const val TOP_ADS_IMAGE_VIEW_PATH = "v1.3/display"

    fun getTopAdsImageViewUrl(): String {
        return BASE_URL + TOP_ADS_IMAGE_VIEW_PATH
    }
}
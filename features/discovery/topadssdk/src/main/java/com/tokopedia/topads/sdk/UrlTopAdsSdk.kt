package com.tokopedia.topads.sdk

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.url.TokopediaUrl.Companion.getInstance

object UrlTopAdsSdk {

    fun getTopAdsImageViewUrl(): String {
       return when (TokopediaUrl.getInstance().TYPE) {
            Env.STAGING -> "https://gql-staging.tokopedia.com/graphql/ta"
            else -> "https://gql.tokopedia.com/graphql/ta"
        }
    }

    var TOP_ADS_BASE_URL = getInstance().TA
}
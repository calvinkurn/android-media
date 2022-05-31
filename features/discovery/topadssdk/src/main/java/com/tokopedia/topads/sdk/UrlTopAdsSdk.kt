package com.tokopedia.topads.sdk

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

object UrlTopAdsSdk {

    fun getTopAdsImageViewUrl(): String {
       return when (TokopediaUrl.getInstance().TYPE) {
            Env.STAGING -> "https://gql-staging.tokopedia.com/graphql/ta"
            else -> "https://gql.tokopedia.com/graphql/ta"
        }
    }
}
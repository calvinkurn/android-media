package com.tokopedia.url

import com.tokopedia.config.GlobalConfig

object GlobalUrl {

    val GRAPHQL: String by lazy {
        url("https://gql.tokopedia.com/",
            "https://gql-staging.tokopedia.com/")
    }

    private fun url(urlLive: String, urlStag: String): String {
        return if (GlobalConfig.isLive()) {
            urlLive
        } else {
            urlStag
        }
    }
}
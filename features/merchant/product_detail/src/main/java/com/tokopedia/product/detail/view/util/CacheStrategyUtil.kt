package com.tokopedia.product.detail.view.util

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy

object CacheStrategyUtil {

    const val EXPIRY_TIME_MULTIPLIER = 5

    fun getCacheStrategy(forceRefresh: Boolean, cacheAge: Int = EXPIRY_TIME_MULTIPLIER): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST)
            .setExpiryTime(cacheAge * GraphqlConstant.ExpiryTimes.MINUTE_1.`val`())
            .setSessionIncluded(true)
            .build()
    }
}

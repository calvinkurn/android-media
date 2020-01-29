package com.tokopedia.product.detail.view.util

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy

object CacheStrategyUtil {

    fun getCacheStrategy(forceRefresh: Boolean): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST)
                .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.MINUTE_1.`val`())
                .setSessionIncluded(true)
                .build()
    }

}
package com.tokopedia.variant_common.util

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy

/**
 * Created by Yehezkiel on 08/03/20
 */

object VariantUtil {
    fun getCacheStrategy(forceRefresh: Boolean): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST)
                .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.MINUTE_1.`val`())
                .setSessionIncluded(true)
                .build()
    }
}
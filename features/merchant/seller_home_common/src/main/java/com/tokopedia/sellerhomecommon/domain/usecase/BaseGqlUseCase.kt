package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 21/05/20
 */

abstract class BaseGqlUseCase<T : Any> : UseCase<T>() {

    var params: RequestParams = RequestParams.create()
    var isFirstLoad: Boolean = true
    protected var cacheStrategy: GraphqlCacheStrategy = getAlwaysCloudCacheStrategy()

    inline fun <reified T> GraphqlResponse.getData(): T {
        return this.getData(T::class.java)
    }

    protected fun getAlwaysCloudCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
            .setExpiryTime(GraphqlConstant.ExpiryTimes.DAY.`val`())
            .setSessionIncluded(true)
            .build()
    }

    protected fun getCacheOnlyCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY)
            .setExpiryTime(GraphqlConstant.ExpiryTimes.DAY.`val`())
            .setSessionIncluded(true)
            .build()
    }

    fun setUseCache(useCache: Boolean) {
        cacheStrategy = if (useCache) {
            getCacheOnlyCacheStrategy()
        } else {
            getAlwaysCloudCacheStrategy()
        }
    }
}
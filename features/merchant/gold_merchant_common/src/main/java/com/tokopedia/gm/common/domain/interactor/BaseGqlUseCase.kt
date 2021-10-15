package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 08/03/21
 */

abstract class BaseGqlUseCase<T : Any> : UseCase<T>() {

    var params: RequestParams = RequestParams.create()

    protected var cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.NONE).build()
        private set

    protected inline fun<reified T> T.getClassName() = T::class.java.name

    inline fun <reified T> GraphqlResponse.getData(): T {
        return this.getData(T::class.java)
    }

    fun setCacheStrategy(cacheStrategy: GraphqlCacheStrategy) {
        this.cacheStrategy = cacheStrategy
    }

    private fun getAlwaysCloudCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
            .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
            .build()
    }

    private fun getCacheFirstCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
            .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
            .build()
    }

    fun getCacheStrategy(useCache: Boolean): GraphqlCacheStrategy {
        return if (useCache) {
            getCacheFirstCacheStrategy()
        } else {
            getAlwaysCloudCacheStrategy()
        }
    }
}
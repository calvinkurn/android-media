package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams

abstract class BaseGraphqlUseCase<T : Any>(graphqlRepository: GraphqlRepository) : GraphqlUseCase<T>(graphqlRepository) {
    protected var params: RequestParams = RequestParams.EMPTY
    var isFirstLoad: Boolean = true

    abstract suspend fun executeOnBackground(useCache: Boolean): T

    inline fun <reified T> GraphqlResponse.getData(): T {
        return this.getData(T::class.java)
    }

    private fun getAlwaysCloudCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
                .build()
    }

    private fun getCacheOnlyCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
                .build()
    }

    fun getCacheStrategy(useCache: Boolean): GraphqlCacheStrategy {
        return if (useCache) {
            getCacheOnlyCacheStrategy()
        } else {
            getAlwaysCloudCacheStrategy()
        }
    }
}
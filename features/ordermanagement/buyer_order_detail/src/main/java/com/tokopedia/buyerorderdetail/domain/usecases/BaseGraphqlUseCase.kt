package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.domain.flow.FlowUseCase
import java.util.concurrent.TimeUnit

abstract class BaseGraphqlUseCase<Input, Output>(
    dispatchers: CoroutineDispatchers
) : FlowUseCase<Input, Output>(dispatchers.io) {

    companion object {
        private const val DEFAULT_CACHE_EXPIRY_TIME_IN_MINUTE = 30L
    }

    private fun getCacheExpiryTime(): Long = TimeUnit.MINUTES.toMillis(
        DEFAULT_CACHE_EXPIRY_TIME_IN_MINUTE
    )

    protected fun getCacheStrategy(shouldCheckCache: Boolean): GraphqlCacheStrategy {
        return if (shouldCheckCache) {
            GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(getCacheExpiryTime())
                .setSessionIncluded(true)
                .build()
        } else {
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
                .setExpiryTime(getCacheExpiryTime())
                .setSessionIncluded(true)
                .build()
        }
    }
}

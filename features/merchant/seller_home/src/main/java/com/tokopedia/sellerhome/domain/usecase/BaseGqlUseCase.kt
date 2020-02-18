package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 2020-02-17
 */

abstract class BaseGqlUseCase<T : Any> : UseCase<T>() {

    var params: RequestParams = RequestParams.EMPTY
    var isForceCloud = true

    fun getCacheStrategy(expiryTimes: Long = GraphqlConstant.ExpiryTimes.MINUTE_30.`val`()): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder((if (isForceCloud) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST))
                .setExpiryTime(expiryTimes)
                .setSessionIncluded(true)
                .build()
    }
}
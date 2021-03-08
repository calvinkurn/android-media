package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 08/03/21
 */

abstract class BaseGqlUseCase<T : Any> : UseCase<T>() {

    var params: RequestParams = RequestParams.EMPTY
    protected var cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.NONE).build()
        private set

    inline fun <reified T> GraphqlResponse.getData(): T {
        return this.getData(T::class.java)
    }

    fun setCacheStrategy(cacheStrategy: GraphqlCacheStrategy) {
        this.cacheStrategy = cacheStrategy
    }
}
package com.tokopedia.settingnotif.usersetting.base

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams

object BaseRequestUseCase {

    val cacheStrategy: GraphqlCacheStrategy by lazy(LazyThreadSafetyMode.NONE) {
        GraphqlCacheStrategy
                .Builder(CacheType.NONE)
                .build()
    }

    suspend inline fun <reified T> execute(
            query: String,
            repository: SettingRepository,
            requestParams: RequestParams = RequestParams.EMPTY
    ): T {
        val request = GraphqlRequest(query, T::class.java, requestParams.parameters)
        val response = repository.getResponse(listOf(request), cacheStrategy)
        val error = response.getError(T::class.java)

        if (error == null || error.isEmpty()) {
            return response.getData(T::class.java) as T
        } else {
            throw MessageErrorException(
                    error.mapNotNull {
                        it.message
                    }.joinToString(separator = ", ")
            )
        }
    }

}
package com.tokopedia.flashsale.management.domain

import android.util.Log
import com.tokopedia.flashsale.management.data.RequestError
import com.tokopedia.flashsale.management.data.ResponseError
import com.tokopedia.flashsale.management.data.Result
import com.tokopedia.flashsale.management.data.Success
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import java.lang.IllegalArgumentException


class SingleGraphqlUseCaseKt<out T : Any>(private val tClass: Class<T>) {
    private var mCacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.NONE).build()
    private var mRequest: GraphqlRequest? = null
    get

    private val graphqlUseCase = GraphqlUseCase()

    fun setRequest(request: GraphqlRequest) {
        mRequest = request
    }

    fun setCacheStrategy(cacheStrategy: GraphqlCacheStrategy) {
        mCacheStrategy = cacheStrategy
    }

    fun getResponse(): Result<T> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(mRequest)
        graphqlUseCase.setCacheStrategy(mCacheStrategy)
        return if (mRequest == null) {
            RequestError(IllegalArgumentException("Request can't be null"))
        } else {
            try {
                val response = graphqlUseCase.getData(RequestParams.EMPTY)
                val error = response.getError(tClass)

                if (error.isEmpty()) {
                    Success(response.getData<T>(tClass))
                } else {
                    Log.e(javaClass.name, error.toString())
                    ResponseError(error.map { it.message })
                }
            } catch (e: Throwable) {
                RequestError(e)
            }
        }

    }
}
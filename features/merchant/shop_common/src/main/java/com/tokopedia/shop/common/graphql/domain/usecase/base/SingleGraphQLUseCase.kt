package com.tokopedia.shop.common.graphql.domain.usecase.base

import android.content.Context
import android.text.TextUtils

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.*
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.HashMap

import rx.Observable
import rx.functions.Func1

abstract class SingleGraphQLUseCase<T>(private val context: Context, private val tClass: Class<T>) : UseCase<T>() {
    private val graphqlUseCase: GraphqlUseCase

    protected abstract val graphQLRawResId: Int
    // default, always use network.
    // do not change to false. Other use-cases might not handling force network when swipe refresh.
    var forceNetwork: Boolean = true

    var useCache: Boolean
        get() = !forceNetwork
        set(value) {
            forceNetwork = value
        }

    init {
        this.graphqlUseCase = GraphqlUseCase()
    }

    override fun createObservable(requestParams: RequestParams): Observable<T> {

        val variables = createGraphQLVariable(requestParams)

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                graphQLRawResId), tClass, variables)

        graphqlUseCase.setCacheStrategy(createGraphQLCacheStrategy())

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.create()).flatMap { graphqlResponse ->
            val data = graphqlResponse.getData<T>(tClass)
            val graphqlErrorList = graphqlResponse.getError(tClass)
            if (graphqlErrorList != null && graphqlErrorList.size > 0) {
                val graphqlError = graphqlErrorList[0]
                val errorMessage = graphqlError.message
                if (TextUtils.isEmpty(errorMessage)) {
                    Observable.just(data)
                } else {
                    Observable.error(MessageErrorException(errorMessage))
                }
            } else {
                forceNetwork = false
                Observable.just(data)
            }
        }

    }

    protected open fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
        return HashMap()
    }

    protected fun createGraphQLCacheStrategy(): GraphqlCacheStrategy? {
        if (forceNetwork) {
            return null
        } else {
            return GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).apply {
                setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
                setSessionIncluded(true)
            }.build();
        }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphqlUseCase.unsubscribe()
    }
}

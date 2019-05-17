package com.tokopedia.instantloan.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.data.model.response.GqlFilterDataResponse
import rx.Subscriber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class GetFilterDataUseCase @Inject constructor(@ApplicationContext context: Context) {

    val graphqlUseCase = GraphqlUseCase()
    val mContext = context

    fun unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe()
        }
    }

    private val cacheDuration: Long = TimeUnit.HOURS.toSeconds(1)

    fun execute(subscriber: Subscriber<GraphqlResponse>) {

        graphqlUseCase.clearRequest()

        val usableRequestMap = HashMap<String, Any>()
        val graphqlRequestForUsable = GraphqlRequest(
                GraphqlHelper.loadRawString(mContext.getResources(), R.raw.query_filter_data),
                GqlFilterDataResponse::class.java, usableRequestMap, false)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setExpiryTime(cacheDuration).setSessionIncluded(false).build()
        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.addRequest(graphqlRequestForUsable)
        graphqlUseCase.execute(subscriber)

    }
}
package com.tokopedia.saldodetails.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.cacheDuration
import com.tokopedia.saldodetails.response.model.GqlWithdrawalTickerResponse
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class GetTickerWithdrawalMessageUseCase @Inject
constructor(@ApplicationContext val context: Context) {

    private val graphqlUseCase = GraphqlUseCase()

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()

        val usableRequestMap = HashMap<String, Any>()
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, com.tokopedia.saldodetails.R.raw.query_withdrawal_ticker),
                GqlWithdrawalTickerResponse::class.java,
                usableRequestMap, false)

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setExpiryTime(cacheDuration).setSessionIncluded(true).build()

        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }
}

package com.tokopedia.saldodetails.feature_saldo_detail.domain.usecase

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.cacheDuration
import com.tokopedia.saldodetails.commom.di.GqlQueryModule.Companion.SALDO_WITHDRAWAL_TICKER_QUERY
import com.tokopedia.saldodetails.commom.utils.GqlUseCaseWrapper
import com.tokopedia.saldodetails.feature_saldo_detail.domain.data.GqlWithdrawalTickerResponse
import javax.inject.Inject
import javax.inject.Named

class GetTickerWithdrawalMessageUseCase @Inject constructor(
        @Named(SALDO_WITHDRAWAL_TICKER_QUERY)
        val queryString: String,
        val graphqlWrapper: GqlUseCaseWrapper
) {

    suspend fun getResponse(): GqlWithdrawalTickerResponse {
        val usableRequestMap = HashMap<String, Any>()
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setExpiryTime(cacheDuration).setSessionIncluded(true).build()

        return graphqlWrapper.getResponse(GqlWithdrawalTickerResponse::class.java, queryString, usableRequestMap, cacheStrategy)
    }

}

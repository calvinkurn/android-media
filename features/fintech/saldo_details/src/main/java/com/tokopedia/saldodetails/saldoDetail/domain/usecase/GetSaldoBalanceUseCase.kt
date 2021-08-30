package com.tokopedia.saldodetails.saldoDetail.domain.usecase

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.commom.di.module.GqlQueryModule
import com.tokopedia.saldodetails.commom.utils.GqlUseCaseWrapper
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlSaldoBalanceResponse
import javax.inject.Inject
import javax.inject.Named


class GetSaldoBalanceUseCase @Inject constructor(
        @Named(GqlQueryModule.MERCHANT_SALDO_BALANCE_QUERY)
        val saldoBalanceQuery: String,
        val gqlUseCaseWrapper: GqlUseCaseWrapper
) {

    suspend fun getResponse(): GqlSaldoBalanceResponse {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setExpiryTime(SaldoDetailsConstants.cacheDuration).setSessionIncluded(true).build()
        return gqlUseCaseWrapper.getResponse(GqlSaldoBalanceResponse::class.java, saldoBalanceQuery, HashMap(), cacheStrategy)
    }
}

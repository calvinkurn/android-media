package com.tokopedia.saldodetails.usecase

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.saldodetails.data.GqlUseCaseWrapper
import com.tokopedia.saldodetails.di.GqlQueryModule
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse
import javax.inject.Inject
import javax.inject.Named

class GetMerchantSaldoDetails @Inject constructor(
        @Named(GqlQueryModule.MERCHANT_SALDO_DETAIL_QUERY)
        val queryString: String,
        val gqlUseCaseWrapper: GqlUseCaseWrapper
) {

    suspend fun getResponse(): GqlMerchantSaldoDetailsResponse {
        val variables = HashMap<String, Any>()
        return gqlUseCaseWrapper.getResponse(GqlMerchantSaldoDetailsResponse::class.java, queryString,
                variables, GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
    }
}

package com.tokopedia.saldodetails.usecase

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.saldodetails.data.GqlUseCaseWrapper
import com.tokopedia.saldodetails.di.GqlQueryModule
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditDetailsResponse
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class GetMerchantCreditDetails @Inject constructor(
        @Named(GqlQueryModule.MERCHANT_CREDIT_DETAIL_QUERY)
        val queryString: String,
        val gqlusecasewrapper: GqlUseCaseWrapper
) {

    suspend fun execute(): GqlMerchantCreditDetailsResponse {
        val variables = HashMap<String, Any>()
        return gqlusecasewrapper.getResponse(GqlMerchantCreditDetailsResponse::class.java,
                queryString, variables, GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
    }
}

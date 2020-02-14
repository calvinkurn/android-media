package com.tokopedia.saldodetails.usecase

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.saldodetails.data.GqlUseCaseWrapper
import com.tokopedia.saldodetails.di.GqlQueryModule
import com.tokopedia.saldodetails.response.model.GqlMclLateCountResponse
import javax.inject.Inject
import javax.inject.Named

class GetMCLLateCountUseCase @Inject constructor(
        @Named(GqlQueryModule.MERCHANT_CREDIT_LATE_COUNT_QUERY)
        val queryString: String,
        val gqlusecasewrapper: GqlUseCaseWrapper
) {
    suspend fun getResponse(): GqlMclLateCountResponse {
        val variables = HashMap<String, Any>()
        return gqlusecasewrapper.getResponse(GqlMclLateCountResponse::class.java,
                queryString, variables, GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

}


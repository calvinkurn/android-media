package com.tokopedia.saldodetails.saldoDetail.domain.usecase

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.saldodetails.commom.di.module.GqlQueryModule
import com.tokopedia.saldodetails.commom.utils.GqlUseCaseWrapper
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlMclLateCountResponse
import javax.inject.Inject
import javax.inject.Named

class GetMCLLateCountUseCase @Inject constructor(
        @Named(GqlQueryModule.MERCHANT_CREDIT_LATE_COUNT_QUERY)
        val queryString: String,
        val gqlusecasewrapper: GqlUseCaseWrapper
) {
    suspend fun getResponse(): GqlMclLateCountResponse {
        val variables = HashMap<String, Any>()
        return gqlusecasewrapper.getResponse(
            GqlMclLateCountResponse::class.java,
                queryString, variables, GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

}


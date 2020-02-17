package com.tokopedia.saldodetails.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.saldodetails.di.GqlQueryModule
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditDetailsResponse
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse
import javax.inject.Inject
import javax.inject.Named

class GetMerchantFinancialStatus @Inject constructor(
        @Named(GqlQueryModule.MERCHANT_CREDIT_DETAIL_QUERY)
        val creditDetailQueryString: String,
        @Named(GqlQueryModule.MERCHANT_SALDO_DETAIL_QUERY)
        val saldoDetailQueryString: String,
        val graphqlUseCase: MultiRequestGraphqlUseCase
) {

    suspend fun getResponse(): GraphqlResponse{
        val map = HashMap<String, Any>()
        val graphqlRequest = GraphqlRequest(saldoDetailQueryString,
                GqlMerchantSaldoDetailsResponse::class.java, map, false)
        graphqlUseCase.addRequest(graphqlRequest)

        val graphqlMCLRequest = GraphqlRequest(creditDetailQueryString,
                GqlMerchantCreditDetailsResponse::class.java, map, false)
        graphqlUseCase.addRequest(graphqlMCLRequest)

        return graphqlUseCase.executeOnBackground()
    }

}

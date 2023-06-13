package com.tokopedia.deals.checkout.domain

import com.tokopedia.common_entertainment.data.DealsCheckoutResponse
import com.tokopedia.common_entertainment.data.DealsGeneral
import com.tokopedia.deals.checkout.domain.query.CheckoutGeneralV2Mutation
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class DealsCheckoutGeneralV2UseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DealsCheckoutResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(CheckoutGeneralV2Mutation())
        setTypeClass(DealsCheckoutResponse::class.java)
    }

    suspend fun execute(checkoutParam: DealsGeneral): DealsCheckoutResponse {
        setRequestParams(createRequestParam(checkoutParam))
        return executeOnBackground()
    }

    private fun createRequestParam(checkoutParam: DealsGeneral) = HashMap<String, Any>().apply {
        put(PARAM, checkoutParam)
    }

    companion object {
        private const val PARAM = "params"
    }
}

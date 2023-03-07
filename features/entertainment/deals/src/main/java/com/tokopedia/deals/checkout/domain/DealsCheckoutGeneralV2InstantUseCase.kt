package com.tokopedia.deals.checkout.domain

import com.tokopedia.common_entertainment.data.DealsCheckoutInstantResponse
import com.tokopedia.common_entertainment.data.DealsInstant
import com.tokopedia.deals.checkout.domain.query.CheckoutGeneralV2InstantMutation
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class DealsCheckoutGeneralV2InstantUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DealsCheckoutInstantResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(CheckoutGeneralV2InstantMutation())
        setTypeClass(DealsCheckoutInstantResponse::class.java)
    }

    suspend fun execute(checkoutInstantParam: DealsInstant): DealsCheckoutInstantResponse {
        setRequestParams(createRequestParam(checkoutInstantParam))
        return executeOnBackground()
    }

    private fun createRequestParam(checkoutInstantParam: DealsInstant) = HashMap<String, Any>().apply {
        put(PARAM, checkoutInstantParam)
    }

    companion object {
        private const val PARAM = "params"
    }
}

package com.tokopedia.digital_checkout.usecase

import com.tokopedia.digital_checkout.data.DigitalCheckoutQueries
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import javax.inject.Inject

@GqlQuery("DigitalCartQuery", DigitalCheckoutQueries.RECHARGE_GET_CART_QUERY)
class DigitalGetCartUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<RechargeGetCart.Response>(graphqlRepository) {

    init {
        setTypeClass(RechargeGetCart.Response::class.java)
        setGraphqlQuery(DigitalCartQuery())
    }

    fun onExecute(
        params: Map<String, Any>,
        onSuccess: (RechargeGetCart.Response) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        setRequestParams(params)
        execute(onSuccess, onError)
    }

    override suspend fun executeOnBackground(): RechargeGetCart.Response {
        GraphqlClient.moduleName = RECHARGE_MODULE_NAME
        return super.executeOnBackground()
    }

    companion object {
        private const val RECHARGE_MODULE_NAME = "recharge"
        private const val PARAM_CATEGORY_ID = "categoryId"
        fun createParams(categoryId: Int): Map<String, Any> = mapOf(PARAM_CATEGORY_ID to categoryId)
    }
}

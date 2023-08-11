package com.tokopedia.pdpsimulation.paylater.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.common.constants.GQL_PAYLATER_SIMULATION_V3
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterGetSimulation
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterSimulationData
import javax.inject.Inject

@GqlQuery("PayLaterSimulationDataQuery", GQL_PAYLATER_SIMULATION_V3)
class PayLaterSimulationV3UseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<PayLaterSimulationData>(graphqlRepository) {

    fun getPayLaterSimulationDetails(
        onSuccess: (PayLaterGetSimulation) -> Unit,
        onError: (Throwable) -> Unit,
        amount: Double,
        productId: String,
        shopId: String,
        parentId: String,
        categoryId: String,
    ) {
        try {
            this.setTypeClass(PayLaterSimulationData::class.java)
            this.setRequestParams(getRequestParams(amount, productId, shopId, parentId, categoryId))
            this.setGraphqlQuery(PayLaterSimulationDataQuery.GQL_QUERY)
            this.execute(
                { result ->
                    onSuccess(result.paylaterGetSimulation)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(
        amount: Double,
        productId: String,
        shopId: String,
        parentId: String,
        categoryId: String
    ): MutableMap<String, Any?> {
        return mutableMapOf(
            "request" to mutableMapOf(
                PARAM_PRODUCT_AMOUNT to amount,
                PARAM_PRODUCT_ID to productId,
                PARAM_SHOP_ID_V2 to shopId,
                PARAM_PARENT_PRODUCT_ID to parentId,
                PARAM_CATEGORY_ID to categoryId
            )
        )
    }

    companion object {
        const val PARAM_PRODUCT_AMOUNT = "amount"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_SHOP_ID_V2 = "shop_id_v2"
        const val PARAM_PARENT_PRODUCT_ID = "parent_product_id"
        const val PARAM_CATEGORY_ID = "category_id"
    }
}

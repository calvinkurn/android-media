package com.tokopedia.pdpsimulation.paylater.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.common.constants.GQL_PAYLATER_SIMULATION_V2
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterGetSimulation
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterSimulationData
import javax.inject.Inject

@GqlQuery("PayLaterAvailableOptionData", GQL_PAYLATER_SIMULATION_V2)
class PayLaterSimulationV2UseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<PayLaterSimulationData>(graphqlRepository) {


    fun getPayLaterProductDetails(
        onSuccess: (PayLaterGetSimulation) -> Unit,
        onError: (Throwable) -> Unit,
        amount: Long
    ) {
        try {
            this.setTypeClass(PayLaterSimulationData::class.java)
            this.setRequestParams(getRequestParams(amount))
            this.setGraphqlQuery(PayLaterAvailableOptionData.GQL_QUERY)
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


    private fun getRequestParams(amount: Long): MutableMap<String, Any?> {
        return mutableMapOf("request" to mutableMapOf(PARAM_PRODUCT_AMOUNT to amount.toDouble()))
    }

    companion object {
        const val PARAM_PRODUCT_AMOUNT = "amount"
    }
}
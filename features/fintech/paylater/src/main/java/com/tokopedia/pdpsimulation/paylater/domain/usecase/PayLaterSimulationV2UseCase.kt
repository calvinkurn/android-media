package com.tokopedia.pdpsimulation.paylater.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.common.constants.GQL_PAYLATER_SIMULATION_V2
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterSimulationBaseResponse
import com.tokopedia.pdpsimulation.paylater.domain.model.PaylaterGetSimulationV2
import javax.inject.Inject

@GqlQuery("PayLaterAvailableOptionData", GQL_PAYLATER_SIMULATION_V2)
class PayLaterSimulationV2UseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<PayLaterSimulationBaseResponse>(graphqlRepository) {


    fun getPayLaterProductDetails(
        onSuccess: (PaylaterGetSimulationV2?) -> Unit,
        onError: (Throwable) -> Unit,
        amount: Long
    ) {
        try {
            this.setTypeClass(PayLaterSimulationBaseResponse::class.java)
            this.setRequestParams(getRequestParams(amount))
            this.setGraphqlQuery(PayLaterAvailableOptionData.GQL_QUERY)
            this.execute(
                { result ->
                    onSuccess(result.data.PaylaterGetSimulationV2)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(amount: Long): Map<String, Float> {
        return mapOf(PARAM_PRODUCT_AMOUNT to amount.toFloat())
    }

    companion object {
        const val PARAM_PRODUCT_AMOUNT = "Amount"
    }
}
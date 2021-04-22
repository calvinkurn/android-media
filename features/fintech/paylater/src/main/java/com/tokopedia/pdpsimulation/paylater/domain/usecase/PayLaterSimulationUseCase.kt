package com.tokopedia.pdpsimulation.paylater.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.common.constants.GQL_PAY_LATER_SIMULATION
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterGetSimulationResponse
import javax.inject.Inject

@GqlQuery("PayLaterSimulationQuery", GQL_PAY_LATER_SIMULATION)
class PayLaterSimulationUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<PayLaterGetSimulationResponse>(graphqlRepository) {

    fun getSimulationData(
            onSuccess: (PayLaterGetSimulationResponse?) -> Unit,
            onError: (Throwable) -> Unit, amount: Long,
    ) {
        try {
            this.setTypeClass(PayLaterGetSimulationResponse::class.java)
            this.setRequestParams(getRequestParams(amount))
            this.setGraphqlQuery(PayLaterSimulationQuery.GQL_QUERY)
            this.execute(
                    { result ->
                        onSuccess(result)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(amount: Long): Map<String, Any> {
        return mapOf(PARAM_PRODUCT_AMOUNT to amount)
    }

    companion object {
        const val PARAM_PRODUCT_AMOUNT = "amount"
    }

}

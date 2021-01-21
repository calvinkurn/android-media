package com.tokopedia.paylater.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.paylater.domain.model.CreditCardGetSimulationResponse
import com.tokopedia.paylater.domain.model.PdpCreditCardSimulation
import com.tokopedia.paylater.domain.query.GQL_CREDIT_CARD_SIMULATION
import javax.inject.Inject

@GqlQuery("CreditCardSimulationQuery", GQL_CREDIT_CARD_SIMULATION)
class CreditCardSimulationUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<CreditCardGetSimulationResponse>(graphqlRepository) {

    fun getCreditCardSimulationData(
            onSuccess: (PdpCreditCardSimulation?) -> Unit,
            onError: (Throwable) -> Unit, amount: Float,
    ) {
        try {
            this.setTypeClass(CreditCardGetSimulationResponse::class.java)
            this.setRequestParams(getRequestParams(amount))
            this.setGraphqlQuery(CreditCardSimulationQuery.GQL_QUERY)
            this.execute(
                    { result ->
                        onSuccess(result.pdpCreditCardSimulationResult)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(amount: Float): Map<String, Float> {
        return mapOf(PARAM_PRODUCT_AMOUNT to 10000000.0f)
    }

    companion object {
        const val PARAM_PRODUCT_AMOUNT = "Amount"
    }

}

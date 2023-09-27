package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.EPharmacyCartCheckoutGeneralQuery
import com.tokopedia.epharmacy.network.params.CheckoutCartGeneralParams
import com.tokopedia.epharmacy.network.response.EPharmacyCartGeneralCheckoutResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyCheckoutCartGeneralUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyCartGeneralCheckoutResponse>(graphqlRepository){

    fun getEPharmacyCheckoutData(
        onSuccess: (EPharmacyCartGeneralCheckoutResponse) -> Unit,
        onError: (Throwable) -> Unit,
        params: CheckoutCartGeneralParams
    ) {
        try {
            this.setTypeClass(EPharmacyCartGeneralCheckoutResponse::class.java)
            this.setGraphqlQuery(EPharmacyCartCheckoutGeneralQuery)
            this.setRequestParams(createRequestParams(params))
            this.execute(
                { result ->
                    onSuccess(result)
                },
                { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun createRequestParams(params: CheckoutCartGeneralParams): Map<String, Any> {
        return mapOf<String, Any>(
            PARAMS to params
        )
    }

    companion object {
        const val PARAMS = "params"
    }
}

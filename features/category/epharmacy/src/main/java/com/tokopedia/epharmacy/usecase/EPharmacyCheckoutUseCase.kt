package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.EPharmacyCheckoutQuery
import com.tokopedia.epharmacy.network.params.CartGeneralAddToCartInstantParams
import com.tokopedia.epharmacy.network.response.EPharmacyCheckoutResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyCheckoutUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyCheckoutResponse>(graphqlRepository){

    fun getEPharmacyCheckoutData(
        onSuccess: (EPharmacyCheckoutResponse) -> Unit,
        onError: (Throwable) -> Unit,
        params: CartGeneralAddToCartInstantParams
    ) {
        try {
            this.setTypeClass(EPharmacyCheckoutResponse::class.java)
            this.setGraphqlQuery(EPharmacyCheckoutQuery)
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

    private fun createRequestParams(params: CartGeneralAddToCartInstantParams): Map<String, Any> {
        return mapOf<String, Any>(
            PARAM_BUSINESS_DATA to params.businessData
        )
    }

    companion object {
        const val PARAM_BUSINESS_DATA = "business_data"
    }
}

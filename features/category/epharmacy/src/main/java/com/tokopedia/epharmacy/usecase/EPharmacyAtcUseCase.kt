package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.EPharmacyAtcQuery
import com.tokopedia.epharmacy.network.params.CartGeneralAddToCartInstantParams
import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyAtcUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyAtcInstantResponse>(graphqlRepository){

    fun getEPharmacyAtcData(
        onSuccess: (EPharmacyAtcInstantResponse) -> Unit,
        onError: (Throwable) -> Unit,
        params: CartGeneralAddToCartInstantParams
    ) {
        try {
            this.setTypeClass(EPharmacyAtcInstantResponse::class.java)
            this.setGraphqlQuery(EPharmacyAtcQuery)
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

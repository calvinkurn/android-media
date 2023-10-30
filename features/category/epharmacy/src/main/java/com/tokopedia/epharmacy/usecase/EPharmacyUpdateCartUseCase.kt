package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.EPharmacyUpdateCartQuery
import com.tokopedia.epharmacy.network.request.EPharmacyUpdateCartParam
import com.tokopedia.epharmacy.network.response.EPharmacyUpdateCartResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyUpdateCartUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyUpdateCartResponse>(graphqlRepository) {

    fun updateEPharmacyCart(
        onSuccess: (EPharmacyUpdateCartResponse) -> Unit,
        onError: (Throwable) -> Unit,
        params: EPharmacyUpdateCartParam
    ) {
        try {
            this.setTypeClass(EPharmacyUpdateCartResponse::class.java)
            this.setRequestParams(getRequestParams(params))
            this.setGraphqlQuery(EPharmacyUpdateCartQuery)
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

    private fun getRequestParams(params: EPharmacyUpdateCartParam): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_INPUT] = params.input
        return requestMap
    }

    companion object {
        const val PARAM_INPUT = "input"
    }
}

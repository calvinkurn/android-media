package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.EPharmacyAtcQuery
import com.tokopedia.epharmacy.network.response.EPharmacyVerifyConsultationResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyVerifyConsultationOrderUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyVerifyConsultationResponse>(graphqlRepository) {

    fun getEPharmacyVerifyConsultationOrder(
        onSuccess: (EPharmacyVerifyConsultationResponse) -> Unit,
        onError: (Throwable) -> Unit,
        tokoConsultationId: String
    ) {
        try {
            this.setTypeClass(EPharmacyVerifyConsultationResponse::class.java)
            this.setGraphqlQuery(EPharmacyAtcQuery)
            this.setRequestParams(createRequestParams(tokoConsultationId))
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

    private fun createRequestParams(tokoConsultationId: String): Map<String, Any> {
        return mapOf<String, Any>(
            PARAM_INPUT to mapOf<String, Any>(
                PARAM_TOKO_CONSULTATION_ID to tokoConsultationId
            )
        )
    }

    companion object {
        const val PARAM_TOKO_CONSULTATION_ID = "toko_consultation_id"
        const val PARAM_INPUT = "input"
    }
}

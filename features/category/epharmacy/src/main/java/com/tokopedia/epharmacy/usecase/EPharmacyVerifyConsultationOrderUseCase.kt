package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.EPharmacyVerifyConsultationOrder
import com.tokopedia.epharmacy.network.response.EPharmacyVerifyConsultationResponse
import com.tokopedia.epharmacy.utils.EPHARMACY_ANDROID_SOURCE
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyVerifyConsultationOrderUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyVerifyConsultationResponse>(graphqlRepository) {

    fun getEPharmacyVerifyConsultationOrder(
        onSuccess: (EPharmacyVerifyConsultationResponse) -> Unit,
        onError: (Throwable) -> Unit,
        tokoConsultationId: Long,
        singleConsulFlow: Boolean
    ) {
        try {
            this.setTypeClass(EPharmacyVerifyConsultationResponse::class.java)
            this.setGraphqlQuery(EPharmacyVerifyConsultationOrder)
            this.setRequestParams(createRequestParams(tokoConsultationId, singleConsulFlow))
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

    private fun createRequestParams(tokoConsultationId: Long, singleConsulFlow: Boolean): Map<String, Any> {
        return mapOf<String, Any>(
            PARAM_TOKO_CONSULTATION_ID to tokoConsultationId,
            PARAM_SOURCE to EPHARMACY_ANDROID_SOURCE,
            PARAM_SINGLE_CONSUL_FLOW to singleConsulFlow
        )
    }

    companion object {
        const val PARAM_TOKO_CONSULTATION_ID = "toko_consultation_id"
        const val PARAM_SOURCE = "source"
        const val PARAM_SINGLE_CONSUL_FLOW = "single_consul_flow"
    }
}

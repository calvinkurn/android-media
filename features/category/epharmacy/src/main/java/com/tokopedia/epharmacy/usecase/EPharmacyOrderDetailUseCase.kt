package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.EPharmacyGetConsultationOrderDetailsQuery
import com.tokopedia.epharmacy.network.response.EPharmacyOrderDetailResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyOrderDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyOrderDetailResponse>(graphqlRepository){

    fun getEPharmacyOrderDetail(
        onSuccess: (EPharmacyOrderDetailResponse) -> Unit,
        onError: (Throwable) -> Unit,
        tConsultationId: String,
        orderId: String
    ) {
        try {
            this.setTypeClass(EPharmacyOrderDetailResponse::class.java)
            this.setGraphqlQuery(EPharmacyGetConsultationOrderDetailsQuery)
            this.setRequestParams(createRequestParams(tConsultationId, orderId))
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

    private fun createRequestParams(tConsultationId: String, orderId: String): Map<String, Any> {
        return mapOf<String, Any>(
            PARAM_TOKO_CONSULATAION_ID to tConsultationId,
            PARAM_ORDER_UUID to orderId,
            PARAM_SOURCE to PARAM_SOURCE_ANDROID

        )
    }

    companion object {
        const val PARAM_TOKO_CONSULATAION_ID = "toko_consultation_id"
        const val PARAM_ORDER_UUID = "order_uuid"
        const val PARAM_SOURCE = "source"
        const val PARAM_SOURCE_ANDROID = "ANDROID"
    }
}

package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.EPharmacyGetConsultationDetailsQuery
import com.tokopedia.epharmacy.network.response.EPharmacyConsultationDetailsResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyGetConsultationDetailsUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyConsultationDetailsResponse>(graphqlRepository) {

    fun getConsultationDetails(
        onSuccess: (EPharmacyConsultationDetailsResponse) -> Unit,
        onError: (Throwable) -> Unit,
        tokoConsultationId: Long
    ) {
        try {
            this.setTypeClass(EPharmacyConsultationDetailsResponse::class.java)
            this.setRequestParams(getRequestParams(tokoConsultationId))
            this.setGraphqlQuery(EPharmacyGetConsultationDetailsQuery)
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

    private fun getRequestParams(tokoConsultationId: Long): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_ID] = tokoConsultationId
        return requestMap
    }

    companion object {
        const val PARAM_ID = "tokopedia_consultation_id"
    }
}

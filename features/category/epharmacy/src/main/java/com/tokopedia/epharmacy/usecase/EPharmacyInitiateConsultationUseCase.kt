package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.EPharmacyInitiateConsultationQuery
import com.tokopedia.epharmacy.network.params.InitiateConsultationParam
import com.tokopedia.epharmacy.network.response.EPharmacyInitiateConsultationResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyInitiateConsultationUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyInitiateConsultationResponse>(graphqlRepository) {

    fun initiateConsultation(
        onSuccess: (String, EPharmacyInitiateConsultationResponse) -> Unit,
        onError: (Throwable) -> Unit,
        params: InitiateConsultationParam
    ) {
        try {
            this.setTypeClass(EPharmacyInitiateConsultationResponse::class.java)
            this.setGraphqlQuery(EPharmacyInitiateConsultationQuery)
            this.setRequestParams(createRequestParams(params))
            this.execute(
                { result ->
                    onSuccess(params.input.epharmacyGroupId, result)
                },
                { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun createRequestParams(params: InitiateConsultationParam): Map<String, Any> {
        return mapOf<String, Any>(
            PARAM_INPUT to params.input
        )
    }

    companion object {
        const val PARAM_INPUT = "input"
    }
}

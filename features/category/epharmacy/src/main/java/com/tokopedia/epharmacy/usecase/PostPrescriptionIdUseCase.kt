package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.GQL_POST_PRESCRIPTION_IDS_QUERY
import com.tokopedia.epharmacy.network.request.ConfirmPrescriptionRequest
import com.tokopedia.epharmacy.network.response.EPharmacyUploadPrescriptionIdsResponse
import com.tokopedia.epharmacy.utils.DEFAULT_ZERO_VALUE
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import rx.functions.ActionN
import javax.inject.Inject

@GqlQuery("GQL_POST_PRESCRIPTION_IDS_QUERY",GQL_POST_PRESCRIPTION_IDS_QUERY)
class PostPrescriptionIdUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyUploadPrescriptionIdsResponse>(graphqlRepository) {

    fun postPrescriptionIds(onSuccess: (EPharmacyUploadPrescriptionIdsResponse) -> Unit,
                            onError: (Throwable) -> Unit,
                            originType :String,
                            id: String, prescriptionIds: ArrayList<Long?>) {
        try {
            this.setTypeClass(EPharmacyUploadPrescriptionIdsResponse::class.java)
            this.setRequestParams(getRequestParams(originType,id,prescriptionIds))
            this.setGraphqlQuery(GQL_POST_PRESCRIPTION_IDS_QUERY)
            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(originType : String ,id: String, prescriptionIds: ArrayList<Long?>): Map<String, Any> {
        val prescriptions = arrayListOf<ConfirmPrescriptionRequest.Input.Prescription>()
        prescriptionIds.forEach { presId ->
            presId?.let {
                if(it != DEFAULT_ZERO_VALUE) {
                    prescriptions.add(ConfirmPrescriptionRequest.Input.Prescription(it))
                }
            }
        }
        return  mapOf<String,Any>(PARAM_INPUT to mutableMapOf<String,Any>().apply {
            put(originType,id)
            put(PARAM_PRESCRIPTIONS,prescriptions)
        })
    }

    companion object {
        private const val PARAM_INPUT = "input"
        private const val PARAM_PRESCRIPTIONS = "prescriptions"
    }
}
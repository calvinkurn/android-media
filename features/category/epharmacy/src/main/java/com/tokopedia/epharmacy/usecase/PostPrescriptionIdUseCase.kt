package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.GQL_POST_PRESCRIPTION_IDS_QUERY
import com.tokopedia.epharmacy.network.request.PrescriptionRequest
import com.tokopedia.epharmacy.network.response.EPharmacyUploadPrescriptionIdsResponse
import com.tokopedia.epharmacy.utils.DEFAULT_ZERO_VALUE
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GqlPostPrescriptionIdsQuery", GQL_POST_PRESCRIPTION_IDS_QUERY)
class PostPrescriptionIdUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyUploadPrescriptionIdsResponse>(graphqlRepository) {

    fun postPrescriptionIdsOrder(
        onSuccess: (EPharmacyUploadPrescriptionIdsResponse) -> Unit,
        onError: (Throwable) -> Unit,
        orderId: Long,
        prescriptionIds: ArrayList<Long?>
    ) {
        try {
            this.setTypeClass(EPharmacyUploadPrescriptionIdsResponse::class.java)
            this.setRequestParams(getRequestParamsOrder(orderId, prescriptionIds))
            this.setGraphqlQuery(GqlPostPrescriptionIdsQuery())
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

    fun postPrescriptionIdsCheckout(
        onSuccess: (EPharmacyUploadPrescriptionIdsResponse) -> Unit,
        onError: (Throwable) -> Unit,
        checkoutId: String,
        prescriptionIds: ArrayList<Long?>
    ) {
        try {
            this.setTypeClass(EPharmacyUploadPrescriptionIdsResponse::class.java)
            this.setRequestParams(getRequestParamsCheckout(checkoutId, prescriptionIds))
            this.setGraphqlQuery(GQL_POST_PRESCRIPTION_IDS_QUERY)
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

    private fun getRequestParamsCheckout(id: String, prescriptionIds: ArrayList<Long?>): Map<String, Any> {
        val inputData = mutableMapOf<String, Any>()
        inputData[PARAM_CHECKOUT_ID] = id
        inputData[PARAM_PRESCRIPTIONS] = getPrescriptionsArray(prescriptionIds)
        return mapOf<String, Any>(PARAM_INPUT to inputData)
    }

    private fun getRequestParamsOrder(id: Long, prescriptionIds: ArrayList<Long?>): Map<String, Any> {
        val inputData = mutableMapOf<String, Any>()
        inputData[PARAM_ORDER_ID] = id
        inputData[PARAM_PRESCRIPTIONS] = getPrescriptionsArray(prescriptionIds)
        return mapOf<String, Any>(PARAM_INPUT to inputData)
    }

    private fun getPrescriptionsArray(prescriptionIds: ArrayList<Long?>): List<PrescriptionRequest> {
        val prescriptions = arrayListOf<PrescriptionRequest>()
        prescriptionIds.forEach { presId ->
            presId?.let {
                if (it != DEFAULT_ZERO_VALUE) {
                    prescriptions.add(PrescriptionRequest(it))
                }
            }
        }
        return prescriptions
    }

    companion object {
        private const val PARAM_INPUT = "input"
        private const val PARAM_PRESCRIPTIONS = "prescriptions"
        private const val PARAM_ORDER_ID = "order_id"
        private const val PARAM_CHECKOUT_ID = "checkout_id"
    }
}

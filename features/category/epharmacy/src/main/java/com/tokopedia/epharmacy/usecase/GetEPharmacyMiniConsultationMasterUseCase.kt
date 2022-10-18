package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.GQL_FETCH_MINI_CONSULTATION_MASTER_QUERY
import com.tokopedia.epharmacy.network.gql.GQL_FETCH_ORDER_DETAILS_QUERY
import com.tokopedia.epharmacy.network.response.EPharmacyMiniConsultationMasterResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GQL_FETCH_MINI_CONSULTATION_MASTER_QUERY",GQL_FETCH_MINI_CONSULTATION_MASTER_QUERY)
class GetEPharmacyMiniConsultationMasterUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyMiniConsultationMasterResponse>(graphqlRepository) {

    fun getEPharmacyOrderDetail(onSuccess: (EPharmacyMiniConsultationMasterResponse) -> Unit,
                                onError: (Throwable) -> Unit,
                                dataType:String,
                                enabler: String) {
        try {
            this.setTypeClass(EPharmacyMiniConsultationMasterResponse::class.java)
            this.setRequestParams(getRequestParams(dataType,enabler))
            this.setGraphqlQuery(GQL_FETCH_ORDER_DETAILS_QUERY)
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

    private fun getRequestParams(dataType: String, enabler: String): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_DATA_TYPE] = dataType
        requestMap[PARAM_ENABLER_NAME] = enabler
        return requestMap
    }

    companion object {
        const val PARAM_DATA_TYPE = "data_type"
        const val PARAM_ENABLER_NAME = "enabler_name"
    }

}

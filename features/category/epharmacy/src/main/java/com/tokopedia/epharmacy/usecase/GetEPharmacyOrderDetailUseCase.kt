package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.GQL_FETCH_ORDER_DETAILS_QUERY
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GQL_FETCH_ORDER_DETAILS_QUERY",GQL_FETCH_ORDER_DETAILS_QUERY)
class GetEPharmacyOrderDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyDataResponse>(graphqlRepository) {

    fun getEPharmacyOrderDetail(onSuccess: (EPharmacyDataResponse) -> Unit,
                                onError: (Throwable) -> Unit,
                                orderId: Long) {
        try {
            this.setTypeClass(EPharmacyDataResponse::class.java)
            this.setRequestParams(getRequestParams(orderId))
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

    private fun getRequestParams(orderId: Long): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_ORDER_ID] = orderId
        return requestMap
    }

    companion object {
        const val PARAM_ORDER_ID = "order_id"
    }

}
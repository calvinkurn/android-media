package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.network.gql.GQL_FETCH_CHECKOUT_DETAILS_QUERY
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GqlFetchCheckoutDetailsQuery",GQL_FETCH_CHECKOUT_DETAILS_QUERY)
class GetEPharmacyCheckoutDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyDataResponse>(graphqlRepository) {

    fun getEPharmacyCheckoutDetail(onSuccess: (EPharmacyDataResponse) -> Unit,
                           onError: (Throwable) -> Unit,
                                   checkoutId: String, source: String) {
        try {
            this.setTypeClass(EPharmacyDataResponse::class.java)
            this.setRequestParams(getRequestParams(checkoutId,source))
            this.setGraphqlQuery(GqlFetchCheckoutDetailsQuery())
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

    private fun getRequestParams(checkoutId: String,source: String): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_CHECKOUT_ID] = checkoutId
        requestMap[PARAM_SOURCE] = source
        return requestMap
    }

    companion object {
        const val PARAM_CHECKOUT_ID = "checkout_id"
        const val PARAM_SOURCE = "source"
    }

}

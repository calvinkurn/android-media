package com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetPrescriptionIdsUseCase @Inject constructor(private val gql: GraphqlUseCase) {

    fun execute(checkoutId: String): Observable<GetPrescriptionIdsResponse> {
        val gqlRequest = GraphqlRequest(
            GET_PRESCRIPTION_IDS_QUERY,
            GetPrescriptionIdsResponse::class.java,
            getRequestParams(checkoutId)
        )
        gql.clearCache()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
            .map {
                val response: GetPrescriptionIdsResponse? = it.getData<GetPrescriptionIdsResponse>(GetPrescriptionIdsResponse::class.java)
                response
                    ?: throw MessageErrorException(it.getError(GetPrescriptionIdsResponse::class.java)[0].message)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getRequestParams(
        checkoutId: String,
        source: String? = SOURCE_CHECKOUT
    ): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_CHECKOUT_ID] = checkoutId
        requestMap[PARAM_SOURCE] = source
        return requestMap
    }

    companion object {
        const val PARAM_CHECKOUT_ID = "checkout_id"
        const val PARAM_SOURCE = "source"

        const val SOURCE_CHECKOUT = "checkout"
        const val SOURCE_OCC = "occ"
    }
}

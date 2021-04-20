package com.tokopedia.checkout.domain.usecase

import com.tokopedia.checkout.data.model.response.ReleaseBookingResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ReleaseBookingUseCase @Inject constructor(private val gql: GraphqlUseCase) {

    fun execute(productId: Long): Observable<ReleaseBookingResponse> {
        val param = mapOf("params" to arrayOf(
                mapOf(
                        "product_id" to productId
                )
        ))
        val gqlRequest = GraphqlRequest(query, ReleaseBookingResponse::class.java, param)

        gql.clearCache()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map {
                    val response: ReleaseBookingResponse? = it.getData<ReleaseBookingResponse>(ReleaseBookingResponse::class.java)
                    response
                            ?: throw MessageErrorException(it.getError(ReleaseBookingResponse::class.java)[0].message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }
}

private val query = """
    mutation release_booking_stock_ocs(${'$'}params:[OCSReleaseBookingStockParam]){
        release_booking_stock_ocs(params:${'$'}params){
            status
            error_message
            data{
                success
                message
            }
        }
    }
""".trimIndent()
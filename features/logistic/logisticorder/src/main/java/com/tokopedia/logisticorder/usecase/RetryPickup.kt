package com.tokopedia.logisticorder.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticorder.di.RawQueryConstant
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import com.tokopedia.logisticorder.usecase.executor.SchedulerProvider
import rx.Observable
import javax.inject.Inject

class RetryPickup @Inject constructor(
        val queryMap: Map<String, String>,
        val gql: GraphqlUseCase,
        val scheduler: SchedulerProvider
) {
    val gqlQuery = queryMap[RawQueryConstant.RETRY_PICKUP]

    fun execute(orderId: String): Observable<RetryBookingResponse> {
        val param = mapOf("id" to orderId)

        val gqlRequest = GraphqlRequest(gqlQuery, RetryBookingResponse::class.java, param)

        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { gqlResponse ->
                    val response: RetryBookingResponse? =
                            gqlResponse.getData(RetryBookingResponse::class.java)
                    response ?: throw MessageErrorException(
                            gqlResponse.getError(RetryBookingResponse::class.java)[0].message)
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }
}
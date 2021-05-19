package com.tokopedia.logisticorder.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticorder.di.RawQueryConstant
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.executor.SchedulerProvider
import rx.Observable
import javax.inject.Inject

class GetRetryAvailability @Inject constructor(
        val queryMap: Map<String, String>,
        val gql: GraphqlUseCase,
        val scheduler: SchedulerProvider
) {
    private val gqlQuery = queryMap[RawQueryConstant.GET_RETRY_AVAILABILITY]

    fun execute(orderId: String): Observable<RetryAvailabilityResponse> {
        val param = mapOf("id" to orderId)
        val gqlRequest = GraphqlRequest(gqlQuery, RetryAvailabilityResponse::class.java, param)

        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { gqlResponse ->
                    val response: RetryAvailabilityResponse? =
                            gqlResponse.getData(RetryAvailabilityResponse::class.java)
                    response
                            ?: throw MessageErrorException(gqlResponse.getError(RetryAvailabilityResponse::class.java)[0].message)
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }
}
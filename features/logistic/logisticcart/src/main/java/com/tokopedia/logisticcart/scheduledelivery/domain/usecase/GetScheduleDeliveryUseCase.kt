package com.tokopedia.logisticcart.scheduledelivery.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.request.ScheduleDeliveryParam
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.response.ScheduleDeliveryRatesResponse
import com.tokopedia.logisticcart.shipping.usecase.DEFAULT_ERROR_MESSAGE
import com.tokopedia.logisticcart.shipping.usecase.scheduleDeliveryRatesQuery
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import javax.inject.Inject

@Deprecated("please use coroutine use case GetScheduleDeliveryCoroutineUseCase")
class GetScheduleDeliveryUseCase @Inject constructor(
    private val scheduler: SchedulerProvider
) {

    private var gql: GraphqlUseCase? = null

    fun execute(param: ScheduleDeliveryParam): Observable<ScheduleDeliveryRatesResponse> {
        val query = scheduleDeliveryRatesQuery()
        val gqlRequest =
            GraphqlRequest(query, ScheduleDeliveryRatesResponse::class.java, param.toMap())
        // Need to init usecase here to prevent request cleared since this usecase will be called multiple time in a very tight interval of each call.
        // Will consider this as tech debt until find a proper solution
        val gql = GraphqlUseCase()
        this.gql = gql
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
            .map { graphqlResponse: GraphqlResponse ->
                val response: ScheduleDeliveryRatesResponse =
                    graphqlResponse.getData<ScheduleDeliveryRatesResponse>(
                        ScheduleDeliveryRatesResponse::class.java
                    ) ?: throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
                response
            }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql?.unsubscribe()
    }
}

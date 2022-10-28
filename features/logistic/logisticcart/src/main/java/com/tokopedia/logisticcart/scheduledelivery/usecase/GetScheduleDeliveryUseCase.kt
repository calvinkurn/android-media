package com.tokopedia.logisticcart.scheduledelivery.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.scheduledelivery.model.ScheduleDeliveryParam
import com.tokopedia.logisticcart.scheduledelivery.model.ScheduleDeliveryResponse
import com.tokopedia.logisticcart.shipping.usecase.scheduleDeliveryQuery
import rx.Observable
import javax.inject.Inject

class GetScheduleDeliveryUseCase @Inject constructor(
    private val scheduler: SchedulerProvider
) {

    private var gql: GraphqlUseCase? = null

    fun execute(param: ScheduleDeliveryParam): Observable<ScheduleDeliveryResponse> {
        val query = scheduleDeliveryQuery()
        val gqlRequest = GraphqlRequest(query, RatesGqlResponse::class.java, param.toMap())
        // Need to init usecase here to prevent request cleared since this usecase will be called multiple time in a very tight interval of each call.
        // Will consider this as tech debt until find a proper solution
        val gql = GraphqlUseCase()
        this.gql = gql
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
            .map { graphqlResponse: GraphqlResponse ->
                val response: ScheduleDeliveryResponse =
                    graphqlResponse.getData<ScheduleDeliveryResponse>(ScheduleDeliveryResponse::class.java)
                        ?: ScheduleDeliveryResponse()
                response
            }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql?.unsubscribe()
    }
}

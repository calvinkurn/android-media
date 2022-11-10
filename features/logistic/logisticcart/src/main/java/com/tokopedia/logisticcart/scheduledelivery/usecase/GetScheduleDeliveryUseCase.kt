package com.tokopedia.logisticcart.scheduledelivery.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.scheduledelivery.model.ScheduleDeliveryParam
import com.tokopedia.logisticcart.scheduledelivery.model.ScheduleDeliveryRatesResponse
import com.tokopedia.logisticcart.shipping.usecase.scheduleDeliveryRatesQuery
import rx.Observable
import javax.inject.Inject

class GetScheduleDeliveryUseCase @Inject constructor(
    private val scheduler: SchedulerProvider
) {

    private var gql: GraphqlUseCase? = null

    fun execute(param: ScheduleDeliveryParam): Observable<ScheduleDeliveryRatesResponse> {
        val query = scheduleDeliveryRatesQuery()
        val gqlRequest = GraphqlRequest(query, RatesGqlResponse::class.java, param.toMap())
        // Need to init usecase here to prevent request cleared since this usecase will be called multiple time in a very tight interval of each call.
        // Will consider this as tech debt until find a proper solution
        val gql = GraphqlUseCase()
        this.gql = gql
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
            .map { graphqlResponse: GraphqlResponse ->
                val response: ScheduleDeliveryRatesResponse =
                    graphqlResponse.getData<ScheduleDeliveryRatesResponse>(ScheduleDeliveryRatesResponse::class.java)
                        ?: ScheduleDeliveryRatesResponse()
                response
            }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql?.unsubscribe()
    }
}

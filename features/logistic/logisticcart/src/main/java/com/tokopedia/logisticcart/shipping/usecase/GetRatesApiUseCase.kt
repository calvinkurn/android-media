package com.tokopedia.logisticcart.shipping.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesApiGqlResponse
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import javax.inject.Inject

@Deprecated("please use coroutine use case GetRatesApiCoroutineUseCase")
class GetRatesApiUseCase @Inject constructor(
    private val converter: ShippingDurationConverter,
    private val gql: GraphqlUseCase,
    private val scheduler: SchedulerProvider
) {

    fun execute(param: RatesParam): Observable<ShippingRecommendationData> {
        val query = ratesQuery(QUERY_RATESV3_API)
        val gqlRequest = GraphqlRequest(
            query,
            RatesApiGqlResponse::class.java,
            mapOf(
                "param" to param.toMap()
            )
        )

        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
            .map { graphqlResponse: GraphqlResponse ->
                val response: RatesApiGqlResponse? =
                    graphqlResponse.getData<RatesApiGqlResponse>(RatesApiGqlResponse::class.java)
                response?.let {
                    converter.convertModel(it.ratesData)
                } ?: throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
            }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }
}

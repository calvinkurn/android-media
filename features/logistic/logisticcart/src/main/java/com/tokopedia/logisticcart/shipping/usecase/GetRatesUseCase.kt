package com.tokopedia.logisticcart.shipping.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.GraphqlHelper
import rx.Observable
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val converter: ShippingDurationConverter,
        private val scheduler: SchedulerProvider) {

    private var gql: GraphqlUseCase? = null

    fun execute(param: RatesParam): Observable<ShippingRecommendationData> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.ratesv3)
        val gqlRequest = GraphqlRequest(query, RatesGqlResponse::class.java, mapOf(
                "param" to param.toMap())
        )

        // Need to init usecase here to prevent request cleared since this usecase will be called multiple time in a very tight interval of each call.
        // Will consider this as tech debt until find a proper solution
        val gql = GraphqlUseCase()
        this.gql = gql
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { graphqlResponse: GraphqlResponse ->
                    val response: RatesGqlResponse =
                            graphqlResponse.getData<RatesGqlResponse>(RatesGqlResponse::class.java)
                                    ?: throw MessageErrorException(context.getString(R.string.default_request_error_unknown))
                    converter.convertModel(response.ratesData)
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql?.unsubscribe()
    }

}
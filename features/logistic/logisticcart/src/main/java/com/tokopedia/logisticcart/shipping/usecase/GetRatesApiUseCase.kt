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
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesApiGqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.GraphqlHelper
import rx.Observable
import javax.inject.Inject

class GetRatesApiUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val converter: ShippingDurationConverter,
        private val gql: GraphqlUseCase,
        private val scheduler: SchedulerProvider) {

    fun execute(param: RatesParam): Observable<ShippingRecommendationData> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.ratesv3api)
        val gqlRequest = GraphqlRequest(query, RatesApiGqlResponse::class.java, mapOf(
                "param" to param.toMap())
        )

        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { graphqlResponse: GraphqlResponse ->
                    val response: RatesApiGqlResponse? =
                            graphqlResponse.getData<RatesApiGqlResponse>(RatesApiGqlResponse::class.java)
                    response?.let {
                        converter.convertModel(it.ratesData)
                    } ?: throw MessageErrorException(context.getString(R.string.default_request_error_unknown))
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }

}
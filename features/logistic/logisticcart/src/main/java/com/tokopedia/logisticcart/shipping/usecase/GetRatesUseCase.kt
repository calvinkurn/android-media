package com.tokopedia.logisticcart.shipping.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.remoteconfig.GraphqlHelper
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val converter: ShippingDurationConverter,
        private val gql: GraphqlUseCase) {

    fun execute(param: RatesParam): Observable<RatesModel> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.ratesv3)
        val gqlRequest = GraphqlRequest(query, RatesGqlResponse::class.java, mapOf(
                "param" to param.toMap())
        )

        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { graphqlResponse: GraphqlResponse ->
                    val response = graphqlResponse.getData<RatesGqlResponse>(RatesGqlResponse::class.java)
                    converter.convertModel(response.ratesData)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }

}
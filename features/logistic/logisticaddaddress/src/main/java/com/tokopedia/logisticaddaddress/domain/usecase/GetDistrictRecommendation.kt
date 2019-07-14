package com.tokopedia.logisticaddaddress.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetDistrictRecommendation @Inject constructor(
        @ApplicationContext val context: Context,
        val gql: GraphqlUseCase,
        val mapper: DistrictRecommendationMapper) {

    fun execute(query: String, page: Int): Observable<AddressResponse> {
        val param: Map<String, String> = mapOf(
                "query" to query,
                "page" to page.toString()
        )
        val gqlQuery = GraphqlHelper.loadRawString(context.resources, R.raw.district_recommendation_new)
        val gqlRequest = GraphqlRequest(gqlQuery, DistrictRecommendationResponse::class.java, param)

        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { gqlResponse ->
                    val response: DistrictRecommendationResponse? =
                            gqlResponse.getData(DistrictRecommendationResponse::class.java)
                    response ?: throw MessageErrorException(
                            gqlResponse.getError(DistrictRecommendationResponse::class.java)[0].message
                    )
                }
                .map { mapper.transformViewModel(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }

}
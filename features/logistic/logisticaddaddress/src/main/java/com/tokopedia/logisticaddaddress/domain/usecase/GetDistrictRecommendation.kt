package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.di.RawQueryConstant
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetDistrictRecommendation @Inject constructor(
        val queryMap: Map<String, String>,
        val gql: GraphqlUseCase,
        val mapper: DistrictRecommendationMapper) {

    private val gqlQuery: String = queryMap[RawQueryConstant.GET_DISTRICT_RECOMMENDATION]
            ?: throw QueryNotFoundException()

    fun execute(query: String, page: Int): Observable<AddressResponse> {
        val param: Map<String, String> = mapOf(
                "query" to query,
                "page" to page.toString()
        )
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
                .map { mapper.transform(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }

}
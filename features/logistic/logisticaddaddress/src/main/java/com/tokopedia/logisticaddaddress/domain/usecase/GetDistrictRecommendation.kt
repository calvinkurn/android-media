package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.di.RawQueryConstant
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import javax.inject.Inject

class GetDistrictRecommendation @Inject constructor(
        val queryMap: Map<String, String>,
        val gql: GraphqlUseCase,
        val scheduler: SchedulerProvider) {

    private val gqlQuery: String = queryMap[RawQueryConstant.GET_DISTRICT_RECOMMENDATION]
            ?: throw QueryNotFoundException()

    fun execute(query: String, page: Int): Observable<DistrictRecommendationResponse> {
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
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }

}
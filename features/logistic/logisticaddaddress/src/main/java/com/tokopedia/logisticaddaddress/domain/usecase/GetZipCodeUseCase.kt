package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictZipcodes
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import javax.inject.Inject

class GetZipCodeUseCase
@Inject constructor(val gql: GraphqlUseCase, val scheduler: SchedulerProvider) {

    fun execute(districtId: String): Observable<DistrictZipcodes> {
        val param = mapOf(
                "query" to districtId,
                "page" to "1"
        )
        val gqlRequest = GraphqlRequest(KeroLogisticQuery.getDistrictDetails,
                DistrictZipcodes::class.java, param)
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { gqlResponse ->
                    val response: DistrictZipcodes? =
                            gqlResponse.getData(DistrictZipcodes::class.java)
                    response ?: throw MessageErrorException(
                            gqlResponse.getError(DistrictZipcodes::class.java)[0].message
                    )
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }

}


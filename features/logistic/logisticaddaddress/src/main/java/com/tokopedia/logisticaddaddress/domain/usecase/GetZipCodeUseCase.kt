package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import javax.inject.Inject

class GetZipCodeUseCase
@Inject constructor(val gql: GraphqlUseCase, val scheduler: SchedulerProvider) {

    fun execute(districtId: String): Observable<DistrictRecommendationResponse> {
        val param = mapOf(
                "query" to districtId,
                "page" to "1"
        )
        val gqlRequest = GraphqlRequest(query_kero_get_district_details,
                DistrictRecommendationResponse::class.java, param)
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

val query_kero_get_district_details = """
query KeroDistrictQuery(${'$'}query: String, ${'$'}page: String){
  kero_get_district_details(query: ${'$'}query, page: ${'$'}page) {
    district {
      district_id
      district_name
      city_id
      city_name
      province_id
      province_name
      zip_code
    }
    next_available
  }
}
""".trimIndent()


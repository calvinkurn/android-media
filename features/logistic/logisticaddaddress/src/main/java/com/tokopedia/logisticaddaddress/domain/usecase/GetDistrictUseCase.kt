package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.data.query.GetDistrictQuery
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.model.get_district.GetDistrictResponse
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import javax.inject.Inject

class GetDistrictUseCase
@Inject constructor(
        private val gql: GraphqlUseCase,
        private val scheduler: SchedulerProvider,
        private val mapper: GetDistrictMapper
) {

    fun execute(placeId: String): Observable<GetDistrictDataUiModel> {
        val param = mapOf(
                "param" to placeId
        )
        val gqlRequest = GraphqlRequest(GetDistrictQuery.keroPlacesGetDistrict,
                GetDistrictResponse::class.java, param)
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { gqlResponse ->
                    val response: GetDistrictResponse? =
                            gqlResponse.getData(GetDistrictResponse::class.java)
                    response ?: throw MessageErrorException(
                            gqlResponse.getError(GetDistrictResponse::class.java)[0].message)
                }
                .map { response -> mapper.map(response) }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }
}
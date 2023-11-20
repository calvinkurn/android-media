package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.GetDistrictRecomParam
import javax.inject.Inject

class GetDistrictRecommendationCoroutineUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetDistrictRecomParam, DistrictRecommendationResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return KeroLogisticQuery.district_recommendation
    }

    override suspend fun execute(params: GetDistrictRecomParam): DistrictRecommendationResponse {
        return repository.request(graphqlQuery(), params)
    }

    companion object {
        const val FOREIGN_COUNTRY_MESSAGE = "Lokasi di luar Indonesia."
        const val LOCATION_NOT_FOUND_MESSAGE = "Lokasi gagal ditemukan"
    }
}

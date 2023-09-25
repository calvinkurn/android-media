package com.tokopedia.logisticcart.shipping.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesApiGqlResponse
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import javax.inject.Inject

class GetRatesApiCoroutineUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
    private val converter: ShippingDurationConverter
) : CoroutineUseCase<RatesParam, ShippingRecommendationData>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return ratesQuery(QUERY_RATESV3_API)
    }

    override suspend fun execute(params: RatesParam): ShippingRecommendationData {
        val response: RatesApiGqlResponse = graphqlRepository.request(graphqlQuery(), params.toRatesV3ApiParam())
        return converter.convertModel(response.ratesData)
    }
}

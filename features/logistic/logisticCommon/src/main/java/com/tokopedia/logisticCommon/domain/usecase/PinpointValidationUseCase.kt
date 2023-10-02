package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.PinpointValidationResponse
import com.tokopedia.logisticCommon.domain.param.PinpointValidationParam
import javax.inject.Inject

class PinpointValidationUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<PinpointValidationParam, PinpointValidationResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: PinpointValidationParam): PinpointValidationResponse {
        return gql.request(
            graphqlQuery(),
            params
        )
    }

    companion object {
        private const val QUERY =
            """
        mutation pinpoint_validation(${'$'}district_id: Int, ${'$'}latitude: String, ${'$'}longitude: String, ${'$'}postal_code: String) {
            pinpoint_validation(district_id: ${'$'}district_id, latitude: ${'$'}latitude, longitude: ${'$'}longitude, postal_code: ${'$'}postal_code) {
                data {
                    checksum
                    district_id
                    latitude
                    longitude
                    result
                    result_text
                }
            }
        }
    """
    }
}

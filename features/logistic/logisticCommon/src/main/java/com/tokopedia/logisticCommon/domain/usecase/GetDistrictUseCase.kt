package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.logisticCommon.domain.param.GetDistrictParam
import javax.inject.Inject

class GetDistrictUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetDistrictParam, GetDistrictResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return placesGetDistrict
    }

    override suspend fun execute(params: GetDistrictParam): GetDistrictResponse {
        return gql.request(graphqlQuery(), params)
    }

    companion object {
        private const val placesGetDistrict = """
        query KeroPlacesGetDistrict(${'$'}param: String!, ${'$'}err: Boolean, ${'$'}is_manage_address_flow: Boolean) {
          kero_places_get_district(placeid: ${'$'}param, error_data: ${'$'}err, is_manage_address_flow: ${'$'}is_manage_address_flow) {
            error_code
            data {
              title
              formatted_address
              district_id
              city_id
              province_id
              district_name
              city_name
              province_name
              postal_code
              latitude
              longitude
              full_data {
                long_name
                short_name
                types
              }
            }
            status
            message_error
          }
        }
    """
    }
}

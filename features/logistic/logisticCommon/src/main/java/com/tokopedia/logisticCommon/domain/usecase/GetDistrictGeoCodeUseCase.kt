package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.entity.response.AutoFillResponse
import com.tokopedia.logisticCommon.domain.param.GetDistrictGeoCodeParam
import javax.inject.Inject

class GetDistrictGeoCodeUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetDistrictGeoCodeParam, AutoFillResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetDistrictGeoCodeParam): AutoFillResponse {
        return gql.request(graphqlQuery(), params)
    }

    companion object {
        private const val QUERY =
            """query kero_maps_autofill(${'$'}latlng: String!, ${'$'}err: Boolean, ${'$'}is_manage_address_flow: Boolean){
          kero_maps_autofill(latlng: ${'$'}latlng, error_data: ${'$'}err, is_manage_address_flow: ${'$'}is_manage_address_flow) {
            data {
              title
              formatted_address
              city_id
              province_id
              province_name
              district_id
              district_name
              city_name
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
            error_code
          }
        }
    """
    }
}

package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.GetDefaultAddressResponse
import com.tokopedia.logisticCommon.domain.param.GetDefaultAddressParam
import javax.inject.Inject

class GetDefaultAddressUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetDefaultAddressParam, GetDefaultAddressResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetDefaultAddressParam): GetDefaultAddressResponse {
        return gql.request(graphqlQuery(), params)
    }

    companion object {
        private const val QUERY = """
        query KeroAddrGetDefaultAddress(${'$'}source: String!, ${'$'}track_activity: Boolean) {
          KeroAddrGetDefaultAddress(source: ${'$'}source, track_activity: ${'$'}track_activity) {
            data {
              addr_id
              receiver_name
              addr_name
              address_1
              address_2
              postal_code
              province
              city
              district
              phone
              province_name
              city_name
              district_name
              status
              country
              latitude
              longitude
            }
            kero_addr_error {
              code
              detail
            }
            status
            server_process_time
            config
          }
        }
    """
    }
}

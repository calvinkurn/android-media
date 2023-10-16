package com.tokopedia.localizationchooseaddress.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.domain.model.GetDefaultChosenAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import javax.inject.Inject

class GetDefaultChosenAddressUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetDefaultChosenAddressParam, GetDefaultChosenAddressGqlResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetDefaultChosenAddressParam): GetDefaultChosenAddressGqlResponse {
        return gql.request(graphqlQuery(), params)
    }

    companion object {
        private const val QUERY = """
        query KeroAddrGetDefaultChosenAddress(${'$'}lat_long: String!,  ${'$'}source: String!,  ${'$'}is_tokonow_request: Boolean!){
          keroAddrGetDefaultChosenAddress(input: {lat_long: ${'$'}lat_long, source: ${'$'}source, is_tokonow_request: ${'$'}is_tokonow_request}) {
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
            tokonow {
              shop_id
              warehouse_id
              warehouses {
                warehouse_id
                service_type
              }
              service_type
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

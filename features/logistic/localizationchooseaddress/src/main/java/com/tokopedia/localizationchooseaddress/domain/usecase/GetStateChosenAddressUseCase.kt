package com.tokopedia.localizationchooseaddress.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.domain.model.GetChosenAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import javax.inject.Inject

class GetStateChosenAddressUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetChosenAddressParam, GetStateChosenAddressQglResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetChosenAddressParam): GetStateChosenAddressQglResponse {
        return gql.request(graphqlQuery(), params)
    }

    companion object {
        private const val QUERY = """
       query KeroAddrGetStateChosenAddress(${'$'}source: String!, ${'$'}is_tokonow_request: Boolean!){
          keroAddrGetStateChosenAddress(source: ${'$'}source, is_tokonow_request: ${'$'}is_tokonow_request) {
            data {
              addr_id
              receiver_name
              addr_name
              district
              city
              city_name
              district_name
              status
              latitude
              longitude
              postal_code
            }
            tokonow {
              shop_id
              warehouse_id
              warehouses {
                warehouse_id
                service_type
              }
              service_type
              tokonow_last_update
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

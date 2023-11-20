package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.request.AddAddressParam
import com.tokopedia.logisticCommon.data.request.KeroAgentAddressInput
import com.tokopedia.logisticCommon.data.response.AddAddressResponse
import javax.inject.Inject

class AddAddressUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<AddAddressParam, AddAddressResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: AddAddressParam): AddAddressResponse {
        return gql.request(
            graphqlQuery(),
            KeroAgentAddressInput(
                param = params
            )
        )
    }

    companion object {
        private const val QUERY =
            """
        mutation KeroAddAddress(${'$'}input: KeroAgentAddressInput!) {
          kero_add_address(input: ${'$'}input) {
            data {
              addr_id
              is_success
              is_state_chosen_address_changed
              chosen_address { 
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
              }
            }
            status
            config
            server_process_time
          }
        }
    """
    }
}

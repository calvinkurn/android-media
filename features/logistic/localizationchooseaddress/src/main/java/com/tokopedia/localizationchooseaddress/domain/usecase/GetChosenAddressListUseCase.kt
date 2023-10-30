package com.tokopedia.localizationchooseaddress.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.domain.model.GetChosenAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListQglResponse
import javax.inject.Inject

class GetChosenAddressListUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetChosenAddressParam, GetChosenAddressListQglResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetChosenAddressParam): GetChosenAddressListQglResponse {
        return gql.request(graphqlQuery(), params)
    }

    companion object {
        private const val QUERY = """
        query keroAddrGetChosenAddressList(${'$'}source: String!, ${'$'}is_tokonow_request: Boolean!) {
          keroAddrGetChosenAddressList(source: ${'$'}source, is_tokonow_request: ${'$'}is_tokonow_request) {
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
              is_state_chosen_address
              tokonow {
                warehouse_id
                coverage_label
              }
            }
            status
            server_process_time
            config
          }
        }
    """
    }
}

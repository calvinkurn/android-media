package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.AddressResponse
import com.tokopedia.logisticCommon.domain.param.GetAddressParam
import com.tokopedia.logisticCommon.domain.param.KeroGetAddressCornerInput
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetAddressParam, AddressResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetAddressParam): AddressResponse {
        return gql.request(graphqlQuery(), KeroGetAddressCornerInput())
    }

    companion object {
        private const val QUERY =
            """
        query keroAddressCorner(${'$'}input: KeroGetAddressInput){
          keroAddressCorner(input:${'$'}input) {
            status
            config
            server_process_time
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
              country
              province_name
              city_name
              district_name
              latitude
              longitude
              status
              is_primary
              is_active
              is_whitelist
              partner_id
              partner_name
              type
              is_corner
              is_state_chosen_address
              radio_button_checked
              is_shared_address
            }
            token {
              district_recommendation
              ut
            }
            page_info {
              ticker
              button_label
            }
          }
        }
    """
    }
}

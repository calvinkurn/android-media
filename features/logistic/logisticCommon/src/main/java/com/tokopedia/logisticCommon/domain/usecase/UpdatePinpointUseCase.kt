package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.request.UpdatePinpointParam
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import javax.inject.Inject

class UpdatePinpointUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<UpdatePinpointParam, KeroEditAddressResponse.Data>(dispatcher.io) {

    override suspend fun execute(params: UpdatePinpointParam): KeroEditAddressResponse.Data {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return QUERY_EDIT_ADDRESS
    }

    companion object {
        private const val QUERY_EDIT_ADDRESS = """
        mutation editAddress(${'$'}input:KeroAddressInput!) {
          kero_edit_address(input:${'$'}input) {
            data{
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

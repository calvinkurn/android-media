package com.tokopedia.localizationchooseaddress.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.domain.model.KeroAddrSetStateChosenAddressInput
import com.tokopedia.localizationchooseaddress.domain.model.StateChooseAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import javax.inject.Inject

class SetStateChosenAddressFromAddressUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<RecipientAddressModel, SetStateChosenAddressQqlResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: RecipientAddressModel): SetStateChosenAddressQqlResponse {
        val param = StateChooseAddressParam(
            params.addressStatus, params.id.toLong(), params.recipientName,
            params.addressName, params.latitude, params.longitude,
            params.destinationDistrictId.toLong(), params.postalCode,
            true
        )
        return gql.request(graphqlQuery(), KeroAddrSetStateChosenAddressInput(param))
    }

    companion object {
        private const val QUERY = """
        mutation setStateChosenAddress(${'$'}input : KeroAddrSetStateChosenAddressInput!) {
          keroAddrSetStateChosenAddress(input: ${'$'}input) {
            data{
              is_success
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

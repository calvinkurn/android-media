package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.domain.param.GetDetailAddressParam
import com.tokopedia.logisticCommon.domain.param.KeroGetAddressInput
import javax.inject.Inject

open class GetAddressDetailUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetDetailAddressParam, KeroGetAddressResponse.Data>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetDetailAddressParam): KeroGetAddressResponse.Data {
        return gql.request(graphqlQuery(), KeroGetAddressInput(params))
    }

    companion object {
        private const val QUERY =
            """
        query getAddressDetail(${'$'}input: KeroGetAddressInput!){
            kero_get_address(input: ${'$'}input) {
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
                  is_primary
                  is_active
                  is_whitelist
                  address_detail_street
                  address_detail_notes
                }
                status
                server_process_time
                config
            }
        }
    """
    }
}

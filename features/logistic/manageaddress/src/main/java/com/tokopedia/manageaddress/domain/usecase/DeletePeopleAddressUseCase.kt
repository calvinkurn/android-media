package com.tokopedia.manageaddress.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.manageaddress.domain.model.DeleteAddressParam
import com.tokopedia.manageaddress.domain.response.DeletePeopleAddressGqlResponse
import javax.inject.Inject

class DeletePeopleAddressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<DeleteAddressParam, DeletePeopleAddressGqlResponse>(dispatcher.io) {

    override fun graphqlQuery() = QUERY

    override suspend fun execute(params: DeleteAddressParam): DeletePeopleAddressGqlResponse =
        repository.request(graphqlQuery(), params)

    companion object {
        private val QUERY = """
            mutation deleteAddress(${"$"}inputAddressId: Int!, ${"$"}isTokonowRequest: Boolean!, ${"$"}consent_json: String) {
              kero_remove_address(addr_id: ${"$"}inputAddressId, is_tokonow_request: ${"$"}isTokonowRequest, consent_json: ${"$"}consent_json) {
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
        """.trimIndent()
    }
}

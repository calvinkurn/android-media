package com.tokopedia.manageaddress.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.manageaddress.domain.model.DefaultAddressParam
import com.tokopedia.manageaddress.domain.response.SetDefaultPeopleAddressGqlResponse
import javax.inject.Inject

class SetDefaultPeopleAddressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<DefaultAddressParam, SetDefaultPeopleAddressGqlResponse>(dispatcher.io) {

    override fun graphqlQuery() = QUERY

    public override suspend fun execute(params: DefaultAddressParam): SetDefaultPeopleAddressGqlResponse {
        return repository.request(graphqlQuery(), params)
    }

    companion object {

        val QUERY = """
            mutation defaultAddress(${"$"}inputAddressId : Int!, ${"$"}setAsStateChosenAddress: Boolean) {
              kero_set_default_address(addr_id: ${"$"}inputAddressId, set_as_state_chosen_address: ${"$"}setAsStateChosenAddress) {
                data{
                  is_success
                }
                status
                config
                server_process_time
              }
            }
        """.trimIndent()
    }
}


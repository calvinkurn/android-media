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
            mutation deleteAddress(${"$"}inputAddressId: Int!, ${"$"}isTokonowRequest: Boolean!) {
              kero_remove_address(addr_id: ${"$"}inputAddressId, is_tokonow_request: ${"$"}isTokonowRequest) {
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

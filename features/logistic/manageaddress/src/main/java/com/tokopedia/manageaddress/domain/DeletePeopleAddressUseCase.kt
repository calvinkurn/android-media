package com.tokopedia.manageaddress.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.manageaddress.domain.response.DeletePeopleAddressGqlResponse
import javax.inject.Inject

class DeletePeopleAddressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers

) : CoroutineUseCase<Int, DeletePeopleAddressGqlResponse>(dispatcher.io) {

    override fun graphqlQuery() = QUERY

    override suspend fun execute(params: Int): DeletePeopleAddressGqlResponse =
        repository.request(graphqlQuery(), createParams(params))

    private fun createParams(params: Int): Map<String, Any> = mapOf(PARAM_KEY to params)

    companion object {
        private const val PARAM_KEY = "inputAddressId"

        private val QUERY = """
            mutation deleteAddress(${"$"}inputAddressId: Int!) {
              kero_remove_address(addr_id: ${"$"}inputAddressId) {
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
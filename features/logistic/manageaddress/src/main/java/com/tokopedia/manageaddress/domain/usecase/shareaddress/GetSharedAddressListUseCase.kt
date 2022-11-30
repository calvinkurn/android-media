package com.tokopedia.manageaddress.domain.usecase.shareaddress

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.manageaddress.domain.response.shareaddress.GetSharedAddressListResponse
import javax.inject.Inject

class GetSharedAddressListUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, GetSharedAddressListResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query KeroAddrGetSharedAddressList(${'$'}source: String!) {
            KeroAddrGetSharedAddressList(source: ${'$'}source) {
                data {
                    sender_user_id
                    receiver_name
                    addr_name
                    masked_address_1
                    masked_address_2
                    masked_phone
                    masked_latitude
                    masked_longitude
                }
                number_of_request
                number_of_displayed
                message
                kero_addr_error {
                    code
                    detail
                    category
                    message
                }
            }
        }
    """.trimIndent()

    override suspend fun execute(params: String): GetSharedAddressListResponse {
        return repository.request(graphqlQuery(), createParams(params))
    }

    private fun createParams(params: String): Map<String, Any> = mapOf(
        PARAM_SOURCE to params
    )

    companion object {
        private const val PARAM_SOURCE: String = "source"
    }
}

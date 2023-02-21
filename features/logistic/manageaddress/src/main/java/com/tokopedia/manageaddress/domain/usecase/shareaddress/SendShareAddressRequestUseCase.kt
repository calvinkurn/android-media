package com.tokopedia.manageaddress.domain.usecase.shareaddress

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.manageaddress.domain.request.shareaddress.SendShareAddressRequestParam
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroShareAddrRequestResponse
import javax.inject.Inject

class SendShareAddressRequestUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SendShareAddressRequestParam, KeroShareAddrRequestResponse>(dispatcher.io) {

    override suspend fun execute(params: SendShareAddressRequestParam): KeroShareAddrRequestResponse {
        return repository.request(graphqlQuery(), params.toMapParam())
    }

    override fun graphqlQuery(): String = """
        mutation KeroAddrSendShareAddressRequest(${'$'}param: KeroAddrSendShareAddressRequestInput!) {
            KeroAddrSendShareAddressRequest(input: ${'$'}param) {
                number_of_request
                kero_addr_error {
                    code
                    detail
                    category
                    message
                }
            }
        }
    """.trimIndent()
}

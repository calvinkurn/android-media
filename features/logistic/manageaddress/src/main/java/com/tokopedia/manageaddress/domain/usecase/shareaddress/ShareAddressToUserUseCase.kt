package com.tokopedia.manageaddress.domain.usecase.shareaddress

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.manageaddress.domain.request.shareaddress.ShareAddressToUserParam
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroShareAddrToUserResponse
import javax.inject.Inject

class ShareAddressToUserUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ShareAddressToUserParam, KeroShareAddrToUserResponse>(dispatcher.io) {

    override suspend fun execute(params: ShareAddressToUserParam): KeroShareAddrToUserResponse {
        return repository.request(graphqlQuery(), params.toMapParam())
    }

    override fun graphqlQuery(): String = """
        mutation KeroAddrShareAddressToUser(${'$'}param: KeroAddrShareAddressToUserInput!) {
            KeroAddrShareAddressToUser(input: ${'$'}param) {
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

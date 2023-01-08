package com.tokopedia.manageaddress.domain.usecase.shareaddress

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.manageaddress.domain.request.shareaddress.SenderShareAddressParam
import com.tokopedia.manageaddress.domain.response.shareaddress.SaveShareAddressResponse
import javax.inject.Inject

class SaveFromFriendAddressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SenderShareAddressParam, SaveShareAddressResponse>(dispatcher.io) {

    override suspend fun execute(params: SenderShareAddressParam): SaveShareAddressResponse {
        return repository.request(graphqlQuery(), params.toMapParam())
    }

    override fun graphqlQuery(): String = """
        mutation KeroAddrSaveSharedAddress(${'$'}param: KeroAddrSaveSharedAddressInput!) {
            KeroAddrSaveSharedAddress(input: ${'$'}param) {
                is_success
                message
                number_address_saved
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

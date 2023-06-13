package com.tokopedia.manageaddress.domain.usecase.shareaddress

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.manageaddress.domain.request.shareaddress.SelectShareAddressParam
import com.tokopedia.manageaddress.domain.response.shareaddress.SelectShareAddressResponse
import javax.inject.Inject

class SelectShareAddressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SelectShareAddressParam.Param, SelectShareAddressResponse>(dispatcher.io) {

    override suspend fun execute(params: SelectShareAddressParam.Param): SelectShareAddressResponse {
        return repository.request(graphqlQuery(), SelectShareAddressParam(params))
    }

    override fun graphqlQuery(): String = """
        mutation KeroAddrSelectAddressForShareAddressRequest(${'$'}param: KeroAddrSelectAddressToShareInput!) {
            KeroAddrSelectAddressForShareAddressRequest(input: ${'$'}param) {
                is_success
                message
                reply_status
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

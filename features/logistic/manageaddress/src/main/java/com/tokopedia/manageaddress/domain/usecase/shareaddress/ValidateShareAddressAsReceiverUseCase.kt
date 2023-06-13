package com.tokopedia.manageaddress.domain.usecase.shareaddress

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.manageaddress.domain.request.shareaddress.ValidateShareAddressAsReceiverParam
import com.tokopedia.manageaddress.domain.response.shareaddress.ValidateShareAddressAsReceiverResponse
import javax.inject.Inject

class ValidateShareAddressAsReceiverUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ValidateShareAddressAsReceiverParam, ValidateShareAddressAsReceiverResponse>(dispatcher.io) {

    override suspend fun execute(params: ValidateShareAddressAsReceiverParam): ValidateShareAddressAsReceiverResponse {
        return repository.request(graphqlQuery(), params.toMapParam())
    }

    override fun graphqlQuery(): String = """
        query KeroAddrValidateShareAddressRequestAsReceiver(${'$'}sender_user_id: SuperInteger!, ${'$'}source: String!) {
            KeroAddrValidateShareAddressRequestAsReceiver(sender_user_id: ${'$'}sender_user_id, source: ${'$'}source) {
                is_valid
                receiver_user_name
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

package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.domain.request.RequestAddressParam
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import javax.inject.Inject

open class RequestAddressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<RequestAddressParam, ShareAddressResponse>(dispatcher.io) {

    override suspend fun execute(params: RequestAddressParam): ShareAddressResponse {
        return repository.request(graphqlQuery(), createParams(params))
    }

    override fun graphqlQuery(): String = ""

    private fun createParams(params: RequestAddressParam): Map<String, Any> = mapOf(
        PARAM_RECEIVER_USER_ID to params.receiverUserId,
        PARAM_SENDER_PHONE_NUMBER_OR_EMAIL to params.senderPhoneNumberOrEmail
    )

    companion object {
        private const val PARAM_RECEIVER_USER_ID = "receiver_user_id"
        private const val PARAM_SENDER_PHONE_NUMBER_OR_EMAIL = "sender_phone_number_or_email"
    }
}
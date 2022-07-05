package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.domain.request.ShareAddressParam
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import javax.inject.Inject

open class ShareAddressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ShareAddressParam, ShareAddressResponse>(dispatcher.io) {

    override suspend fun execute(params: ShareAddressParam): ShareAddressResponse {
        return repository.request(graphqlQuery(), createParams(params))
    }

    override fun graphqlQuery(): String = ""

    private fun createParams(params: ShareAddressParam): Map<String, Any> = mapOf(
        PARAM_SENDER_USER_ID to params.senderUserId,
        PARAM_SENDER_ADDRESS_ID to params.senderAddressId,
        PARAM_RECEIVER_PHONE_NUMBER_OR_EMAIL to params.receiverPhoneNumberOrEmail,
        PARAM_INITIAL_CHECK to params.initialCheck
    )

    companion object {
        private const val PARAM_SENDER_USER_ID = "sender_user_id"
        private const val PARAM_SENDER_ADDRESS_ID = "sender_address_id"
        private const val PARAM_RECEIVER_PHONE_NUMBER_OR_EMAIL = "receiver_phone_number_or_email"
        private const val PARAM_INITIAL_CHECK = "initial_check"
    }
}
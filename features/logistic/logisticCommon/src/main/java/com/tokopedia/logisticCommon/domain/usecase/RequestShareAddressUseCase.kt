package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.domain.request.RequestShareAddress
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import javax.inject.Inject

open class RequestShareAddressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<RequestShareAddress, ShareAddressResponse>(dispatcher.io) {

    override suspend fun execute(params: RequestShareAddress): ShareAddressResponse {
        return repository.request(graphqlQuery(), createParams(params))
    }

    override fun graphqlQuery(): String = ""

    private fun createParams(params: RequestShareAddress): Map<String, Any> = mapOf(
        PARAM_USER_ID to params.userId,
        PARAM_EMAIL to params.email,
        PARAM_PHONE to params.phone
    )

    companion object {
        private const val PARAM_PHONE = "msisdn"
        private const val PARAM_EMAIL = "email"
        private const val PARAM_USER_ID = "user_id"
    }
}
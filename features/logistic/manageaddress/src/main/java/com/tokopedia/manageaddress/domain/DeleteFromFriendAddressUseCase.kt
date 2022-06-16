package com.tokopedia.manageaddress.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import javax.inject.Inject

open class DeleteFromFriendAddressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, ShareAddressResponse>(dispatcher.io) {

    override suspend fun execute(params: String): ShareAddressResponse {
        return repository.request(graphqlQuery(), createParams(params))
    }

    override fun graphqlQuery(): String = ""

    private fun createParams(params: String): Map<String, Any> = mapOf(
        PARAM_ADDRESS_ID to params
    )

    companion object {
        private const val PARAM_ADDRESS_ID = "address_id"
    }
}
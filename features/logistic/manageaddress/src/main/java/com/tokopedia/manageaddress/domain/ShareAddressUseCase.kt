package com.tokopedia.manageaddress.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import com.tokopedia.manageaddress.domain.model.shareaddress.ShareAddressParam
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
        PARAM_EMAIL to params.email,
        PARAM_PHONE to params.phone,
        PARAM_ADDRESS_ID to params.addressId
    )

    companion object {
        private const val PARAM_PHONE = "msisdn"
        private const val PARAM_EMAIL = "email"
        private const val PARAM_ADDRESS_ID = "address_id"
    }
}
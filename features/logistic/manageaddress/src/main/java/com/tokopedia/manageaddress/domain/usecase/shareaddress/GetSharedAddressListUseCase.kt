package com.tokopedia.manageaddress.domain.usecase.shareaddress

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.manageaddress.domain.model.shareaddress.GetSharedAddressListParam
import com.tokopedia.manageaddress.domain.response.shareaddress.GetSharedAddressListResponse
import javax.inject.Inject

open class GetSharedAddressListUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, GetSharedAddressListResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return KeroLogisticQuery.get_shared_address_list
    }

    override suspend fun execute(params: String): GetSharedAddressListResponse {
        return repository.request(graphqlQuery(), createParams(params))
    }

    private fun createParams(params: String): Map<String, Any> = mapOf(
        PARAM_SOURCE to params
    )

    companion object {
        private const val PARAM_SOURCE: String = "source"
    }
}
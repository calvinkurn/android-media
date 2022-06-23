package com.tokopedia.manageaddress.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticCommon.domain.mapper.AddressCornerMapper
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.request.AddressRequest
import com.tokopedia.logisticCommon.domain.response.GetPeopleAddressResponse
import javax.inject.Inject

open class GetAddressSharedListUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
    private val mapper: AddressCornerMapper
) : CoroutineUseCase<AddressRequest, AddressListModel>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return KeroLogisticQuery.addressCorner
    }

    override suspend fun execute(params: AddressRequest): AddressListModel {
        val data = repository.request<Map<String, Any>, GetPeopleAddressResponse>(graphqlQuery(), createParams(params))
        return mapper.call(data)
    }

    private fun createParams(params: AddressRequest): Map<String, Any> = mapOf(
        PARAM_ADDRESS_USECASE to params
    )

    companion object {
        private const val PARAM_ADDRESS_USECASE: String = "input"
    }
}
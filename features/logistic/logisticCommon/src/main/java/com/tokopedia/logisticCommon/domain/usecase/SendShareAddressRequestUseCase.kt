package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticCommon.domain.request.SendShareAddressRequestParam
import com.tokopedia.logisticCommon.domain.response.shareaddress.KeroShareAddrRequestResponse
import javax.inject.Inject

open class SendShareAddressRequestUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SendShareAddressRequestParam, KeroShareAddrRequestResponse>(dispatcher.io) {

    override suspend fun execute(params: SendShareAddressRequestParam): KeroShareAddrRequestResponse {
        return repository.request(graphqlQuery(), params.toMapParam())
    }

    override fun graphqlQuery(): String = KeroLogisticQuery.kero_send_share_addr_request
}
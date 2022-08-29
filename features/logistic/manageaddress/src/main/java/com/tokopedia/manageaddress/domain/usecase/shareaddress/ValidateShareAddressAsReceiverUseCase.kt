package com.tokopedia.manageaddress.domain.usecase.shareaddress

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.manageaddress.domain.model.shareaddress.ValidateShareAddressAsReceiverParam
import com.tokopedia.manageaddress.domain.response.shareaddress.ValidateShareAddressAsReceiverResponse
import javax.inject.Inject

open class ValidateShareAddressAsReceiverUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ValidateShareAddressAsReceiverParam, ValidateShareAddressAsReceiverResponse>(dispatcher.io) {

    override suspend fun execute(params: ValidateShareAddressAsReceiverParam): ValidateShareAddressAsReceiverResponse {
        return repository.request(graphqlQuery(), params.toMapParam())
    }

    override fun graphqlQuery(): String = KeroLogisticQuery.validate_share_address_request_as_receiver
}
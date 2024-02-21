package com.tokopedia.checkoutpayment.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetRequest
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetPaymentWidgetUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetPaymentWidgetRequest, GetPaymentWidgetResponse>(dispatchers.io) {
    override fun graphqlQuery(): String {
        return ""
    }

    override suspend fun execute(params: GetPaymentWidgetRequest): GetPaymentWidgetResponse {
        return GetPaymentWidgetResponse()
    }
}

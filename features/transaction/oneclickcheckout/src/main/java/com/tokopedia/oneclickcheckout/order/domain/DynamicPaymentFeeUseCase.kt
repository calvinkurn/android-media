package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import javax.inject.Inject

class DynamicPaymentFeeUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    executorDispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, List<OrderPaymentFee>>(executorDispatchers.io) {

    override fun graphqlQuery(): String {
        return ""
    }

    override suspend fun execute(params: Unit): List<OrderPaymentFee> {
        return listOf(OrderPaymentFee("biaya dinamis", 2000.0, true, false, 0, "dinamis"))
    }
}

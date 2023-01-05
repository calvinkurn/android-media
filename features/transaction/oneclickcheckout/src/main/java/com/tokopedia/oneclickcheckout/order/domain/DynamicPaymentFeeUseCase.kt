package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.oneclickcheckout.order.data.payment.PaymentFeeRequest
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import javax.inject.Inject

class DynamicPaymentFeeUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    executorDispatchers: CoroutineDispatchers
) : CoroutineUseCase<PaymentFeeRequest, List<OrderPaymentFee>>(executorDispatchers.io) {

    override fun graphqlQuery(): String {
        return QUERY
    }

    @GqlQuery("GetPaymentFeeQuery", QUERY)
    override suspend fun execute(params: PaymentFeeRequest): List<OrderPaymentFee> {
//        val response = graphqlRepository.response(
//            listOf(
//                GraphqlRequest(
//                    GetPaymentFeeQuery(),
//                    PaymentFeeGqlResponse::class.java,
//                    mapOf(
//                        PARAM to params
//                    )
//                )
//            )
//        ).getData<PaymentFeeGqlResponse>(PaymentFeeGqlResponse::class.java)
//        if (!response.response.success) {
//            throw MessageErrorException(response.response.errors.getOrNull(0)?.message)
//        }
//        return response.response.data.map { OrderPaymentFee(it.title, it.fee, it.showTooltip, it.showSlashed, it.slashedFee, it.tooltipInfo) }
        return listOf(OrderPaymentFee("biaya dinamis", 2000.0, true, false, 0, "dinamis"))
    }

    companion object {
        private const val PARAM = "request"

        private const val QUERY = """
            query getPaymentFee(${"$"}request: GetPaymentFeeRequest!) {
                getPaymentFee(request: ${"$"}request) {
                    success
                    errors {
                        code
                        message
                    }
                    data {
                        code
                        title
                        fee
                        show_slashed
                        slashed_fee
                        show_tooltip
                        tooltip_info
                    }
                }
            }
        """
    }
}

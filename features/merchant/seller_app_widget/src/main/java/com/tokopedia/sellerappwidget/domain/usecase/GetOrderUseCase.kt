package com.tokopedia.sellerappwidget.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerappwidget.data.model.GetOrderResponse
import com.tokopedia.sellerappwidget.data.model.InputParameterModel
import com.tokopedia.sellerappwidget.domain.mapper.OrderMapper
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 17/11/20
 */

class GetOrderUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: OrderMapper
) : BaseUseCase<OrderUiModel>() {

    override suspend fun executeOnBackground(): OrderUiModel {
        val gqlRequest = GraphqlRequest(QUERY, GetOrderResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))
        val errors: List<GraphqlError>? = gqlResponse.getError(GetOrderResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetOrderResponse>()
            return mapper.mapRemoteModelToUiModel(data)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val KEY_INPUT = "input"

        /**
         * create params to get order list
         * @param startDateFmt should be date with format dd/MM/yyyy
         * @param endDateFmt should be date with format dd/MM/yyyy
         * @param statusId is order status id, ex : new order is 220, ready to ship is 400, etc
         * @param sortBy to short order by SORT_BY_PAYMENT_DATE_ASCENDING which is 0
         * or SORT_BY_PAYMENT_DATE_ASCENDING which is 2
         * @return `RequestParams`
         * */
        fun createParams(startDateFmt: String, endDateFmt: String, statusId: Int, sortBy: Int): RequestParams {
            val input = InputParameterModel(
                    startDate = startDateFmt,
                    endDate = endDateFmt,
                    sortBy = sortBy,
                    statusList = listOf(statusId)
            )
            return RequestParams.create().apply {
                putObject(KEY_INPUT, input)
            }
        }

        private val QUERY = """
            query OrderList(${'$'}input: OrderListArgs!) {
              orderList(input: ${'$'}input) {
                list {
                  order_id
                  order_status_id
                  deadline_text
                  order_product {
                    product_id
                    product_name
                    picture
                  }
                }
              }
              orderFilterSom {
                status_list {
                  key
                  order_status_amount
                }
              }
            }
        """.trimIndent()
    }
}
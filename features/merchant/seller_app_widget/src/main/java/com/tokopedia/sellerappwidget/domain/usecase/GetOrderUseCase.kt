package com.tokopedia.sellerappwidget.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.data.model.GetOrderResponse
import com.tokopedia.sellerappwidget.data.model.InputParameterModel
import com.tokopedia.sellerappwidget.di.AppWidgetScope
import com.tokopedia.sellerappwidget.domain.mapper.OrderMapper
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/11/20
 */

@AppWidgetScope
class GetOrderUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: OrderMapper
) : BaseUseCase<List<OrderUiModel>>() {

    override suspend fun executeOnBackground(): List<OrderUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetOrderResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))
        val errors: List<GraphqlError>? = gqlResponse.getError(GetOrderResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetOrderResponse>()
            val orderList = data.orderList.list
            return mapper.mapRemoteModelToUiModel(orderList)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val KEY_INPUT = "input"
        private const val LANG_ID = "id"
        private const val FILTER_STATUS = 999

        /**
         * create params to get order list
         * @param startDateFmt should be date with format dd/MM/yyyy
         * @param endDateFmt should be date with format dd/MM/yyyy
         * @return `RequestParams`
         * */
        fun createParams(startDateFmt: String, endDateFmt: String): RequestParams {
            val input = InputParameterModel(
                    batchPage = 0,
                    startDate = startDateFmt,
                    endDate = endDateFmt,
                    filterStatus = FILTER_STATUS,
                    isBuyerRequestCancel = 0,
                    isMobile = true,
                    lang = LANG_ID,
                    nextOrderId = 0,
                    orderTypeList = emptyList(),
                    page = 1,
                    search = "",
                    shippingList = emptyList(),
                    sortBy = 0,
                    statusList = listOf(Const.OrderStatusId.READY_TO_SHIP, Const.OrderStatusId.NEW_ORDER)
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
            }
        """.trimIndent()
    }
}
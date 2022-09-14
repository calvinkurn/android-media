package com.tokopedia.watch.orderlist.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.watch.orderlist.model.InputParameterModel
import com.tokopedia.watch.orderlist.model.OrderListModel
import rx.Observable
import rx.functions.Func1

class GetOrderListUseCase(
    private val graphqlUseCase: GraphqlUseCase,
    private val orderListModelMapper: Func1<GraphqlResponse?, OrderListModel>
): UseCase<OrderListModel>() {
    override fun createObservable(p0: RequestParams?): Observable<OrderListModel> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(mutableListOf(
            createOrderListGqlRequest()
        ))

        val gqlOrderListObservable = graphqlUseCase
            .createObservable(RequestParams.EMPTY)
            .map(orderListModelMapper)

        return gqlOrderListObservable
    }

    @GqlQuery("OrderListQuery", ORDER_LIST_QUERY)
    private fun createOrderListGqlRequest(): GraphqlRequest {
        return GraphqlRequest(
            OrderListQuery.GQL_QUERY,
            OrderListModel::class.java,
            createParams(
                "01/04/2022",
                "27/07/2022",
                220,
                0
            ).parameters
        )
    }

    companion object {
        private const val ORDER_LIST_QUERY = """
            query OrderList(${'$'}input: OrderListArgs!) {
              orderList(input: ${'$'}input) {
                list {
                  order_id
                  order_status_id
                  order_total_price
                  order_date
                  deadline_text
                  courier_type
                  courier_product_name
                  courier_name
                  destination_province
                  order_product {
                    product_id
                    product_name
                    product_qty
                    picture
                    order_note
                  }
                }
              }
            }
        """

        fun createParams(startDateFmt: String, endDateFmt: String, statusId: Int, sortBy: Int): RequestParams {
            val input = InputParameterModel(
                startDate = startDateFmt,
                endDate = endDateFmt,
                sortBy = sortBy,
                statusList = listOf(),
            )
            return RequestParams.create().apply {
                putObject("input", input)
            }
        }
    }
}
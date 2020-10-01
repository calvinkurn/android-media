package com.tokopedia.seller.action.data.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.data.model.Order
import com.tokopedia.seller.action.data.model.SellerActionOrder
import com.tokopedia.seller.action.data.param.SellerActionOrderListParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SliceMainOrderListUseCase @Inject constructor(private val gqlRepository: GraphqlRepository)
    : UseCase<List<Order>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<Order> {
        val request = GraphqlRequest(QUERY, SellerActionOrder::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val errors = response.getError(SellerActionOrder::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<SellerActionOrder>(SellerActionOrder::class.java)
//            return listOf(
//                    Order("", "Ini bisa lho", "10/10/2020", "Reivin Oktavianus", listOf(OrderProduct("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQCfT2lhPa4MLcJ9KozVg6LX5Er10795w0vj9jw-6mjnbP1A3o6d9PrDA7cKg&usqp=CAc")))
//            )
            return data.orderList.orders
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val QUERY = "query OrderList(\$input: OrderListArgs!) {\n" +
                "              orderList(input: \$input) {\n" +
                "                list {\n" +
                "                  order_id\n" +
                "                  status\n" +
                "                  order_date\n" +
                "                  buyer_name\n" +
                "                  order_product {\n" +
                "                    picture\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }"

        private const val INPUT_KEY = "input"

        @JvmStatic
        fun createRequestParam(startDate: String,
                               endDate: String): RequestParams =
                RequestParams.create().apply {
                    putObject(INPUT_KEY, SellerActionOrderListParam().also {
                        it.startDate = startDate
                        it.endDate = endDate
                        it.statusList = listOf(
                                SellerActionConst.Som.STATUS_CODE_ORDER_CREATED,
                                SellerActionConst.Som.STATUS_CODE_ORDER_ORDER_CONFIRMED
                        )}
                    )
                }

    }

}
package com.tokopedia.seller.action.data.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.data.model.SellerActionOrderList
import com.tokopedia.seller.action.data.param.SellerActionOrderListParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SliceMainOrderListUseCase @Inject constructor(private val gqlRepository: GraphqlRepository)
    : UseCase<List<SellerActionOrderList.Data.OrderList.Order>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<SellerActionOrderList.Data.OrderList.Order> {
        val request = GraphqlRequest(QUERY, SellerActionOrderList::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val errors = response.getError(SellerActionOrderList::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<SellerActionOrderList>(SellerActionOrderList::class.java)
            return data.data.orderList.orders
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
                RequestParams().apply {
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
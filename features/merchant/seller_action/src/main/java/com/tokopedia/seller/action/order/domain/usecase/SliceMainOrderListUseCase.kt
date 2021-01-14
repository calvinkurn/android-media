package com.tokopedia.seller.action.order.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.domain.model.SellerActionOrder
import com.tokopedia.seller.action.order.domain.param.SellerActionOrderListParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SliceMainOrderListUseCase @Inject constructor(private val gqlRepository: GraphqlRepository)
    : UseCase<List<Order>>() {

    companion object {
        private const val QUERY = "query OrderList(\$input: OrderListArgs!) {\n" +
                "              orderList(input: \$input) {\n" +
                "                list {\n" +
                "                  order_id\n" +
                "                  status\n" +
                "                  order_status_id\n" +
                "                  deadline_text\n" +
                "                  order_date\n" +
                "                  buyer_name\n" +
                "                  order_product {\n" +
                "                    picture\n" +
                "                    product_name\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }"

        private const val INPUT_KEY = "input"

        @JvmStatic
        fun createRequestParam(startDate: String,
                               endDate: String,
                               statusList: List<Int>): RequestParams =
                RequestParams.create().apply {
                    putObject(INPUT_KEY, SellerActionOrderListParam().also {
                        it.startDate = startDate
                        it.endDate = endDate
                        it.statusList = statusList
                    })
                }
    }

    var params: RequestParams = RequestParams.EMPTY

    private val cacheStrategy by lazy {
        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
    }

    override suspend fun executeOnBackground(): List<Order> {
        val request = GraphqlRequest(QUERY, SellerActionOrder::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request), cacheStrategy)

        val errors = response.getError(SellerActionOrder::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<SellerActionOrder>(SellerActionOrder::class.java)
            return data.orderList.orders
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }

}
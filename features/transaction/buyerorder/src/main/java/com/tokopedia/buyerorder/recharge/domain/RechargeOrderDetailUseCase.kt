package com.tokopedia.buyerorder.recharge.domain

import com.tokopedia.buyerorder.recharge.data.RechargeOrderDetailGQL
import com.tokopedia.buyerorder.recharge.data.request.RechargeOrderDetailRequest
import com.tokopedia.buyerorder.recharge.data.response.RechargeOrderDetail
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 26/10/2021
 */
class RechargeOrderDetailUseCase @Inject constructor(
        private val usecase: MultiRequestGraphqlUseCase
) {

    suspend fun execute(requestParams: RechargeOrderDetailRequest): Result<RechargeOrderDetailModel> {
        val params = mapOf(
                ORDER_CATEGORY to requestParams.orderCategory,
                ORDER_ID to requestParams.orderId,
                DETAIL to DEFAULT_DETAIL,
                ACTION to DEFAULT_ACTION,
                UPSTREAM to (requestParams.upstream ?: "")
        )
        usecase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        usecase.clearRequest()

        return try {
            val graphqlRequest = GraphqlRequest(RechargeOrderDetailGQL.ORDER_DETAIL_QUERY,
                    RechargeOrderDetail.Response::class.java, params)
            usecase.addRequest(graphqlRequest)
            val response = usecase.executeOnBackground().getData<RechargeOrderDetail.Response>(RechargeOrderDetail.Response::class.java)
            val rechargeOrderDetailModel = RechargeOrderDetailMapper.transform(response.orderDetails)
            Success(rechargeOrderDetailModel)
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    companion object {
        private const val ORDER_CATEGORY = "orderCategoryStr"
        private const val ORDER_ID = "orderId"
        private const val DETAIL = "detail"
        private const val ACTION = "action"
        private const val UPSTREAM = "upstream"

        private const val DEFAULT_DETAIL = 1
        private const val DEFAULT_ACTION = 1
    }

}
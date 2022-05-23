package com.tokopedia.buyerorder.recharge.domain

import com.tokopedia.buyerorder.recharge.data.RechargeEmoneyVoidGql
import com.tokopedia.buyerorder.recharge.data.response.RechargeEmoneyVoidResponse
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
class RechargeEmoneyVoidUseCase @Inject constructor(
    private val usecase: MultiRequestGraphqlUseCase
) {

    suspend fun execute(orderId: String): Result<RechargeEmoneyVoidResponse> {
        val params = mapOf(ORDER_ID to orderId)
        usecase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        usecase.clearRequest()

        return try {
            val graphqlRequest = GraphqlRequest(
                RechargeEmoneyVoidGql(),
                RechargeEmoneyVoidResponse.Response::class.java,
                params
            )
            usecase.addRequest(graphqlRequest)
            val response = usecase.executeOnBackground()
                .getData<RechargeEmoneyVoidResponse.Response>(RechargeEmoneyVoidResponse.Response::class.java)
            if (response.rechargeEmoneyVoid.status == 0 ||
                response.rechargeEmoneyVoid.status == STATUS_FAILED_CODE ||
                response.rechargeEmoneyVoid.status == STATUS_ERROR_CODE
            ) {
                Fail(Throwable(response.rechargeEmoneyVoid.message))
            } else {
                val result = response.rechargeEmoneyVoid
                result.isNeedRefresh = true
                Success(result)
            }
        } catch (t: Throwable) {
            Fail(t)
        }
    }

    companion object {
        private const val STATUS_FAILED_CODE = 300
        private const val STATUS_ERROR_CODE = 500

        private const val ORDER_ID = "orderID"
    }

}
package com.tokopedia.sellerorder.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.domain.model.SomAcceptOrder
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_IS_FROM_FINTECH
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SHOP_ID
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 11/05/20.
 */
class SomAcceptOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomAcceptOrder.Data>) {

    init {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(SomAcceptOrder.Data::class.java)
    }

    suspend fun execute(orderId: String, shopId: String): Result<SomAcceptOrder.Data> {
        useCase.setRequestParams(generateParam(orderId, shopId))

        return try {
            val acceptOrder = useCase.executeOnBackground()
            Success(acceptOrder)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(orderId: String, shopId: String): Map<String, Map<String, Any>> {
        val requestAcceptOrderParam = mapOf(PARAM_ORDER_ID to orderId,
                PARAM_SHOP_ID to shopId, PARAM_IS_FROM_FINTECH to false)
        return mapOf(PARAM_INPUT to requestAcceptOrderParam)
    }

    companion object {
        private val QUERY = """
            mutation AcceptOrder(${'$'}input:OrderAcceptRequest!) {
                accept_order(input:${'$'}input){
                    success,
                    message
                 }
            }
        """.trimIndent()
    }
}
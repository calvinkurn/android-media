package com.tokopedia.sellerorder.detail.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_IS_FROM_FINTECH
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SHOP_ID
import com.tokopedia.sellerorder.detail.data.model.SomAcceptOrder
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 11/05/20.
 */
class SomAcceptOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomAcceptOrder.Data>) {

    suspend fun execute(query: String, orderId: String, shopId: String): Result<SomAcceptOrder.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomAcceptOrder.Data::class.java)
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
}
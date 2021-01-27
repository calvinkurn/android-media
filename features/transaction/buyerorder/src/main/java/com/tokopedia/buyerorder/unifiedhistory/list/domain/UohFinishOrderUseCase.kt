package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrderParam
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 02/08/20.
 */
class UohFinishOrderUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(param: UohFinishOrderParam): Result<UohFinishOrder.Data.FinishOrderBuyer> {
        return try {
            val request = GraphqlRequest(QUERY, UohFinishOrder.Data::class.java, generateParam(param))
            val response = gqlRepository.getReseponse(listOf(request)).getSuccessData<UohFinishOrder.Data>()
            Success(response.finishOrderBuyer)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    private fun generateParam(param: UohFinishOrderParam): Map<String, Any?> {
        return mapOf(BuyerConsts.PARAM_INPUT to param)
    }

    companion object {
        val QUERY = """
            mutation FinishOrderBuyer(${'$'}input:FinishOrderBuyerRequest!) {
              finish_order_buyer(input:${'$'}input) {
                success
                message
              }
            }
            """.trimIndent()
    }
}
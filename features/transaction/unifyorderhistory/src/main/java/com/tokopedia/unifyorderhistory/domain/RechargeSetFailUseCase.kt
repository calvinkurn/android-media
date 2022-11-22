package com.tokopedia.unifyorderhistory.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.data.model.RechargeSetFailData
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 10/08/20.
 */
class RechargeSetFailUseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(orderId: Int): Result<RechargeSetFailData.Data> {
        return try {
            val request = GraphqlRequest(QUERY, RechargeSetFailData.Data::class.java, generateParam(orderId))
            val response = gqlRepository.response(listOf(request)).getSuccessData<RechargeSetFailData.Data>()
            Success(response)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    private fun generateParam(orderId: Int): Map<String, Any?> {
        return mapOf(UohConsts.RECHARGE_GQL_PARAM_ORDER_ID to orderId)
    }

    companion object {
        val QUERY = """
            mutation rechargeSetOrderToFail(${'$'}orderId: Int64!) {
              rechargeSetOrderToFailV2(order_id:${'$'}orderId) {
                attributes {
                  user_id
                  order_status
                  is_success
                  error_message
                }
                error
              }
            }
            """.trimIndent()
    }
}

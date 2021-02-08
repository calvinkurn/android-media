package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.LS_PRINT_GQL_PARAM_ACTION
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.LS_PRINT_GQL_PARAM_BUSINESS_CODE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.LS_PRINT_GQL_PARAM_UUID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.LS_PRINT_GQL_PARAM_VALUE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.PARAM_LS_PRINT_BUSINESS_CODE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.PARAM_LS_PRINT_FINISH_ACTION
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.LsPrintData
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 04/08/20.
 */
class LsPrintFinishOrderUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(verticalId: String): Result<LsPrintData.Data> {
        return try {
            val request = GraphqlRequest(QUERY, LsPrintData.Data::class.java, generateParam(verticalId))
            val response = gqlRepository.getReseponse(listOf(request)).getSuccessData<LsPrintData.Data>()
            Success(response)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    private fun generateParam(verticalId: String): Map<String, Any?> {
        return mapOf(LS_PRINT_GQL_PARAM_BUSINESS_CODE to PARAM_LS_PRINT_BUSINESS_CODE,
                LS_PRINT_GQL_PARAM_ACTION to PARAM_LS_PRINT_FINISH_ACTION,
                LS_PRINT_GQL_PARAM_UUID to verticalId,
                LS_PRINT_GQL_PARAM_VALUE to "")
    }

    companion object {
        val QUERY = """
            mutation OrderInternalActionMutation(${'$'}businessCode:String, ${'$'}action:String, ${'$'}uuid:String, ${'$'}value:String) {
              oiaction(businessCode:${'$'}businessCode, action:${'$'}action, uuid:${'$'}uuid, value:${'$'}value) {
                error
                status
                data {
                  message
                }
              }
            }
            """.trimIndent()
    }
}
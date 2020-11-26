package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.LS_PRINT_GQL_PARAM_ACTION
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.LS_PRINT_GQL_PARAM_BUSINESS_CODE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.LS_PRINT_GQL_PARAM_UUID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.LS_PRINT_GQL_PARAM_VALUE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.PARAM_LS_PRINT_BUSINESS_CODE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.PARAM_LS_PRINT_FINISH_ACTION
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.LsPrintData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 04/08/20.
 */
class LsPrintFinishOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<LsPrintData.Data>) {

    suspend fun execute(query: String, verticalId: String): Result<LsPrintData.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(LsPrintData.Data::class.java)
        useCase.setRequestParams(generateParam(verticalId))

        return try {
            val finishOrder = useCase.executeOnBackground()
            Success(finishOrder)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(verticalId: String): Map<String, Any?> {
        return mapOf(LS_PRINT_GQL_PARAM_BUSINESS_CODE to PARAM_LS_PRINT_BUSINESS_CODE,
                     LS_PRINT_GQL_PARAM_ACTION to PARAM_LS_PRINT_FINISH_ACTION,
                     LS_PRINT_GQL_PARAM_UUID to verticalId,
                     LS_PRINT_GQL_PARAM_VALUE to "")
    }
}
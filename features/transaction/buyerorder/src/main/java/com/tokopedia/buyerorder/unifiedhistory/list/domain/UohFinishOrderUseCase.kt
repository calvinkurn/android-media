package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrderParam
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 02/08/20.
 */
class UohFinishOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<UohFinishOrder.Data>) {

    suspend fun execute(query: String, param: UohFinishOrderParam): Result<UohFinishOrder.Data.FinishOrderBuyer> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(UohFinishOrder.Data::class.java)
        useCase.setRequestParams(generateParam(param))

        return try {
            val finishOrder = useCase.executeOnBackground().finishOrderBuyer
            Success(finishOrder)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(param: UohFinishOrderParam): Map<String, Any?> {
        return mapOf(BuyerConsts.PARAM_INPUT to param)
    }
}
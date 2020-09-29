package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListParam
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/07/20.
 */
class UohListUseCase @Inject constructor(private val useCase: GraphqlUseCase<UohListOrder.Data>) {

    suspend fun execute(param: UohListParam, query: String): Result<UohListOrder.Data.UohOrders> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(UohListOrder.Data::class.java)
        useCase.setRequestParams(generateParam(param))

        return try {
            val orderList = useCase.executeOnBackground().uohOrders
            Success(orderList)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(param: UohListParam): Map<String, Any?> {
        return mapOf(BuyerConsts.PARAM_INPUT to param)
    }
}
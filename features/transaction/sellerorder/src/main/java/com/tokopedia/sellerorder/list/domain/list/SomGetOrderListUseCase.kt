package com.tokopedia.sellerorder.list.domain.list

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 09/05/20.
 */
class SomGetOrderListUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomListOrder.Data>) {

    suspend fun execute(param: SomListOrderParam, query: String): Result<SomListOrder.Data.OrderList> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomListOrder.Data::class.java)
        useCase.setRequestParams(generateParam(param))

        return try {
            val orderList = useCase.executeOnBackground().orderList
            Success(orderList)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(param: SomListOrderParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }
}
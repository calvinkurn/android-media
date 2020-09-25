package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.RechargeSetFailData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 10/08/20.
 */
class RechargeSetFailUseCase @Inject constructor(private val useCase: GraphqlUseCase<RechargeSetFailData.Data>) {

    suspend fun execute(query: String, orderId: Int): Result<RechargeSetFailData.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(RechargeSetFailData.Data::class.java)
        useCase.setRequestParams(generateParam(orderId))

        return try {
            val finishOrder = useCase.executeOnBackground()
            Success(finishOrder)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(orderId: Int): Map<String, Any?> {
        return mapOf(UohConsts.RECHARGE_GQL_PARAM_ORDER_ID to orderId)
    }
}
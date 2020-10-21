package com.tokopedia.sellerorder.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.domain.model.SomRejectOrder
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 11/05/20.
 */
class SomRejectOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomRejectOrder.Data>) {

    init {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(SomRejectOrder.Data::class.java)
    }

    suspend fun execute(rejectOrderRequest: SomRejectRequest): Result<SomRejectOrder.Data> {
        useCase.setRequestParams(generateParam(rejectOrderRequest))

        return try {
            val rejectOrder = useCase.executeOnBackground()
            Success(rejectOrder)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(rejectOrderRequest: SomRejectRequest): Map<String, SomRejectRequest> {
        return mapOf(SomConsts.PARAM_INPUT to rejectOrderRequest)
    }

    companion object {
        private val QUERY = """
            mutation RejectOrder(${'$'}input:OrderRejectRequest!) {
                reject_order(input:${'$'}input){
                    success,message
                }
            }
        """.trimIndent()
    }
}
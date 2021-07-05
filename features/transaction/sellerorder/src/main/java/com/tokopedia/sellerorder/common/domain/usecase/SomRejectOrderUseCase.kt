package com.tokopedia.sellerorder.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.domain.model.SomRejectOrderResponse
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 11/05/20.
 */
class SomRejectOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomRejectOrderResponse.Data>) {

    init {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(SomRejectOrderResponse.Data::class.java)
    }

    suspend fun execute(rejectOrderRequestParam: SomRejectRequestParam): SomRejectOrderResponse.Data {
        useCase.setRequestParams(generateParam(rejectOrderRequestParam))
        return useCase.executeOnBackground()
    }

    private fun generateParam(rejectOrderRequestParam: SomRejectRequestParam): Map<String, SomRejectRequestParam> {
        return mapOf(SomConsts.PARAM_INPUT to rejectOrderRequestParam)
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
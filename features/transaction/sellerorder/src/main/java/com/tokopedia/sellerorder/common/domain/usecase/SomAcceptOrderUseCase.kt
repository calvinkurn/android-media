package com.tokopedia.sellerorder.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.domain.model.SomAcceptOrderRequestParam
import com.tokopedia.sellerorder.common.domain.model.SomAcceptOrderResponse
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 11/05/20.
 */
class SomAcceptOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomAcceptOrderResponse.Data>) {

    init {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(SomAcceptOrderResponse.Data::class.java)
    }

    suspend fun execute(): SomAcceptOrderResponse.Data = useCase.executeOnBackground()

    fun setParams(orderId: String, shopId: String) {
        useCase.setRequestParams(RequestParams.create().apply {
            putObject(PARAM_INPUT, SomAcceptOrderRequestParam(orderId, shopId))
        }.parameters)
    }

    companion object {
        private val QUERY = """
            mutation AcceptOrder(${'$'}input:OrderAcceptRequest!) {
                accept_order(input:${'$'}input){
                    success,
                    message
                 }
            }
        """.trimIndent()
    }
}
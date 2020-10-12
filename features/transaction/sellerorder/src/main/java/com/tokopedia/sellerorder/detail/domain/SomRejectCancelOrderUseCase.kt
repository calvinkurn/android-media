package com.tokopedia.sellerorder.detail.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomRejectCancelOrderRequest
import com.tokopedia.sellerorder.detail.data.model.SomRejectCancelOrderResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by yusuf.hendrawan on 24/08/20.
 */

class SomRejectCancelOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomRejectCancelOrderResponse.Data>) {

    init {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(SomRejectCancelOrderResponse.Data::class.java)
    }

    suspend fun execute(rejectOrderRequest: SomRejectCancelOrderRequest): Result<SomRejectCancelOrderResponse.Data> {
        useCase.setRequestParams(generateParam(rejectOrderRequest))

        return try {
            val rejectOrder = useCase.executeOnBackground()
            Success(rejectOrder)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(rejectCancelOrderRequest: SomRejectCancelOrderRequest): Map<String, SomRejectCancelOrderRequest> {
        return mapOf(SomConsts.PARAM_INPUT to rejectCancelOrderRequest)
    }

    companion object {
        const val QUERY = "mutation RejectCancelRequest(${'$'}input: RejectCancelRequestData!) {\n" +
                "              reject_cancel_request(input: ${'$'}input) {\n" +
                "                success\n" +
                "                message\n" +
                "              }\n" +
                "            }"
    }
}
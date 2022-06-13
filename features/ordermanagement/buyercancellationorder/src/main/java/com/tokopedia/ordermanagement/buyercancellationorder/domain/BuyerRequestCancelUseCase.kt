package com.tokopedia.ordermanagement.buyercancellationorder.domain

import com.tokopedia.ordermanagement.buyercancellationorder.data.requestcancel.BuyerRequestCancelData
import com.tokopedia.ordermanagement.buyercancellationorder.data.requestcancel.BuyerRequestCancelParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 16/08/20.
 */
class BuyerRequestCancelUseCase @Inject constructor(private val useCase: GraphqlUseCase<BuyerRequestCancelData.Data>) {

    suspend fun execute(requestCancelParam: BuyerRequestCancelParam): Result<BuyerRequestCancelData.Data> {
        useCase.setGraphqlQuery(getQuery())
        useCase.setTypeClass(BuyerRequestCancelData.Data::class.java)
        useCase.setRequestParams(generateParam(requestCancelParam))

        return try {
            val cancellationReason = useCase.executeOnBackground()
            Success(cancellationReason)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(requestCancelParam: BuyerRequestCancelParam): Map<String, BuyerRequestCancelParam> {
        return mapOf(BuyerConsts.PARAM_INPUT to requestCancelParam)
    }

    private fun getQuery(): String {
        return """
            mutation BuyerRequestCancel(${'$'}input :BuyerRequestCancelRequest!) {
              buyer_request_cancel(input:  ${'$'}input) {
                success
                message
                popup {
                  title
                  body
                }
              }
            }
        """.trimIndent()
    }
}
package com.tokopedia.ordermanagement.buyercancellationorder.domain

import com.tokopedia.ordermanagement.buyercancellationorder.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.ordermanagement.buyercancellationorder.data.instantcancellation.BuyerInstantCancelParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 28/07/20.
 */
class BuyerInstantCancelUseCase @Inject constructor(private val useCase: GraphqlUseCase<BuyerInstantCancelData.Data>) {

    suspend fun execute(instantCancelParam: BuyerInstantCancelParam): Result<BuyerInstantCancelData.Data> {
        useCase.setGraphqlQuery(getQuery())
        useCase.setTypeClass(BuyerInstantCancelData.Data::class.java)
        useCase.setRequestParams(generateParam(instantCancelParam))

        return try {
            val instantCancel = useCase.executeOnBackground()
            Success(instantCancel)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(instantCancelParam: BuyerInstantCancelParam): Map<String, BuyerInstantCancelParam> {
        return mapOf(BuyerConsts.PARAM_INPUT to instantCancelParam)
    }

    private fun getQuery(): String {
        return """
            mutation BuyerInstantCancel(${'$'}input :BuyerInstantCancelRequest!) {
              buyer_instant_cancel(input: ${'$'}input) {
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
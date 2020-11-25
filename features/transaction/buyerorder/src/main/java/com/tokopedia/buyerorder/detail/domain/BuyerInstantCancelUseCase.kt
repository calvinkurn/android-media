package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 28/07/20.
 */
class BuyerInstantCancelUseCase @Inject constructor(private val useCase: GraphqlUseCase<BuyerInstantCancelData.Data>) {

    suspend fun execute(query: String, instantCancelParam: BuyerInstantCancelParam): Result<BuyerInstantCancelData.Data> {
        useCase.setGraphqlQuery(query)
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
}
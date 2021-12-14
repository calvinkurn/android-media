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

    suspend fun execute(query: String, requestCancelParam: BuyerRequestCancelParam): Result<BuyerRequestCancelData.Data> {
        useCase.setGraphqlQuery(query)
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
}
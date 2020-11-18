package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelData
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 18/11/20.
 */
class GetOrderDetailUseCase @Inject constructor(private val useCase: GraphqlUseCase<DetailsData>) {

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
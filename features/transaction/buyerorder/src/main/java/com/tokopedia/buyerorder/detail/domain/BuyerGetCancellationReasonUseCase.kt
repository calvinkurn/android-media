package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.common.util.BuyerConsts.PARAM_INPUT
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/06/20.
 */
class BuyerGetCancellationReasonUseCase @Inject constructor(private val useCase: GraphqlUseCase<BuyerGetCancellationReasonData.Data>) {

    suspend fun execute(query: String, getCancellationReasonParam: BuyerGetCancellationReasonParam): Result<BuyerGetCancellationReasonData.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(BuyerGetCancellationReasonData.Data::class.java)
        useCase.setRequestParams(generateParam(getCancellationReasonParam))

        return try {
            val cancellationReason = useCase.executeOnBackground()
            Success(cancellationReason)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(cancellationReasonParam: BuyerGetCancellationReasonParam): Map<String, BuyerGetCancellationReasonParam> {
        return mapOf(PARAM_INPUT to cancellationReasonParam)
    }
}
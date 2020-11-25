package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.TrainResendEmail
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.TrainResendEmailParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 08/08/20.
 */
class TrainResendEmailUseCase @Inject constructor(private val useCase: GraphqlUseCase<TrainResendEmail.Data>) {

    suspend fun execute(query: String, param: TrainResendEmailParam): Result<TrainResendEmail.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(TrainResendEmail.Data::class.java)
        useCase.setRequestParams(generateParam(param))

        return try {
            val finishOrder = useCase.executeOnBackground()
            Success(finishOrder)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(param: TrainResendEmailParam): Map<String, Any?> {
        return mapOf(BuyerConsts.PARAM_INPUT to param)
    }
}
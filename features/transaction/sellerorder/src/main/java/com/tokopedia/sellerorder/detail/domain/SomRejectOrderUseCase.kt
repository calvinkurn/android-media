package com.tokopedia.sellerorder.detail.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectParam
import com.tokopedia.sellerorder.detail.data.model.SomRejectOrder
import com.tokopedia.sellerorder.detail.data.model.SomRejectRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 11/05/20.
 */
class SomRejectOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomRejectOrder.Data>) {

    suspend fun execute(query: String, rejectOrderRequest: SomRejectRequest): Result<SomRejectOrder.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomRejectOrder.Data::class.java)
        useCase.setRequestParams(generateParam(rejectOrderRequest))

        return try {
            val rejectOrder = useCase.executeOnBackground()
            Success(rejectOrder)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(rejectOrderRequest: SomRejectRequest): Map<String, SomRejectRequest> {
        return mapOf(SomConsts.PARAM_INPUT to rejectOrderRequest)
    }
}
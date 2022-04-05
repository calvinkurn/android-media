package com.tokopedia.sellerorder.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SetDeliveredRequest
import com.tokopedia.sellerorder.detail.data.model.SetDeliveredResponse
import com.tokopedia.sellerorder.detail.domain.query.SomSetDeliveredQuery
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 11/05/20.
 */
class SomSetDeliveredUseCase @Inject constructor(private val useCase: GraphqlUseCase<SetDeliveredResponse>) {

    suspend fun execute(orderId: String, receivedBy: String): Result<SetDeliveredResponse> {
        useCase.setGraphqlQuery(SomSetDeliveredQuery)
        useCase.setTypeClass(SetDeliveredResponse::class.java)
        useCase.setRequestParams(generateParam(orderId, receivedBy))

        return try {
            val setDelivered = useCase.executeOnBackground()
            Success(setDelivered)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(orderId: String, receivedBy: String): Map<String, SetDeliveredRequest> {
        return mapOf(SomConsts.PARAM_INPUT to SetDeliveredRequest(orderId, receivedBy))
    }
}
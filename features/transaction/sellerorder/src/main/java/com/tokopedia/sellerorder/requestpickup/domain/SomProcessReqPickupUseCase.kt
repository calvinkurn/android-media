package com.tokopedia.sellerorder.requestpickup.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/05/20.
 */
class SomProcessReqPickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomProcessReqPickup.Data>) {

    suspend fun execute(query: String, param: SomProcessReqPickupParam): Result<SomProcessReqPickup.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomProcessReqPickup.Data::class.java)
        useCase.setRequestParams(generateParam(param))

        return try {
            val processReqPickup = useCase.executeOnBackground()
            Success(processReqPickup)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(param: SomProcessReqPickupParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }
}
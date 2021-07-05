package com.tokopedia.sellerorder.requestpickup.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/05/20.
 */
class SomConfirmReqPickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomConfirmReqPickup.Data>) {

    suspend fun execute(query: String, param: SomConfirmReqPickupParam): Result<SomConfirmReqPickup.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomConfirmReqPickup.Data::class.java)
        useCase.setRequestParams(generateParam(param))

        return try {
            val confirmReqPickup = useCase.executeOnBackground()
            Success(confirmReqPickup)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(param: SomConfirmReqPickupParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }
}
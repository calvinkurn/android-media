package com.tokopedia.sellerorder.requestpickup.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/05/20.
 */
class SomConfirmReqPickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomConfirmReqPickup.Data>) {

    init {
        useCase.setTypeClass(SomConfirmReqPickup.Data::class.java)
    }

    suspend fun execute(query: String, param: SomConfirmReqPickupParam): SomConfirmReqPickup.Data {
        useCase.setGraphqlQuery(query)
        useCase.setRequestParams(generateParam(param))
        return useCase.executeOnBackground()
    }

    private fun generateParam(param: SomConfirmReqPickupParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }
}
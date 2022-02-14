package com.tokopedia.sellerorder.requestpickup.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickupParam
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/05/20.
 */
class SomProcessReqPickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomProcessReqPickup.Data>) {

    init {
        useCase.setTypeClass(SomProcessReqPickup.Data::class.java)
    }

    suspend fun execute(query: String, param: SomProcessReqPickupParam): SomProcessReqPickup.Data {
        useCase.setGraphqlQuery(query)
        useCase.setRequestParams(generateParam(param))
        return useCase.executeOnBackground()
    }

    private fun generateParam(param: SomProcessReqPickupParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }
}
package com.tokopedia.sellerorder.reschedule_pickup.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import javax.inject.Inject

class GetReschedulePickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<GetReschedulePickupResponse.Data>) {

    init {
        useCase.setTypeClass(GetReschedulePickupResponse.Data::class.java)
    }

    suspend fun execute(param: GetReschedulePickupParam): GetReschedulePickupResponse.Data {
        useCase.setGraphqlQuery(GetReschedulePickupQuery)
        useCase.setRequestParams(generateParam(param))
        return useCase.executeOnBackground()
    }

    private fun generateParam(param: GetReschedulePickupParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }
}
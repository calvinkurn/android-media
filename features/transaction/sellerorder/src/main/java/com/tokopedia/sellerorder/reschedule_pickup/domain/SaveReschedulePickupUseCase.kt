package com.tokopedia.sellerorder.reschedule_pickup.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.reschedule_pickup.data.model.SaveReschedulePickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.SaveReschedulePickupResponse
import javax.inject.Inject

class SaveReschedulePickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<SaveReschedulePickupResponse.Data>) {

    init {
        useCase.setTypeClass(SaveReschedulePickupResponse.Data::class.java)
    }

    suspend fun execute(param: SaveReschedulePickupParam): SaveReschedulePickupResponse.Data {
//        useCase.setGraphqlQuery(SaveReschedulePickupQuery)
//        useCase.setRequestParams(generateParam(param))
//        return useCase.executeOnBackground()
        return SaveReschedulePickupResponse.Data(
                mpLogisticInsertReschedulePickup = SaveReschedulePickupResponse.Data.MpLogisticInsertReschedulePickup(
                    message = "Sepertinya pencarian driver baru sudah dilakukan (maks. 1x) atau masih berlangsung.",
                    status = "999"
                )
            )
    }

    private fun generateParam(param: SaveReschedulePickupParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }
}
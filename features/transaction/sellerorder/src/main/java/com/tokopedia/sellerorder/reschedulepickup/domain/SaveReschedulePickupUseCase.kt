package com.tokopedia.sellerorder.reschedulepickup.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.reschedulepickup.data.model.SaveReschedulePickupParam
import com.tokopedia.sellerorder.reschedulepickup.data.model.SaveReschedulePickupResponse
import javax.inject.Inject

class SaveReschedulePickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<SaveReschedulePickupResponse.Data>) {

    init {
        useCase.setTypeClass(SaveReschedulePickupResponse.Data::class.java)
    }

    @GqlQuery(SaveReschedulePickupMutation, MP_LOGISTIC_INSERT_RESCHEDULE_PICKUP)
    suspend fun execute(param: SaveReschedulePickupParam): SaveReschedulePickupResponse.Data {
        useCase.setGraphqlQuery(SaveReschedulePickupMutation())
        useCase.setRequestParams(generateParam(param))
        return useCase.executeOnBackground()
    }

    private fun generateParam(param: SaveReschedulePickupParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }

    companion object {
        const val SaveReschedulePickupMutation = "SaveReschedulePickupMutation"
        const val MP_LOGISTIC_INSERT_RESCHEDULE_PICKUP = """
            mutation saveReschedulePickup(${'$'}input:MpLogisticInsertReschedulePickupInputs!){
                mpLogisticInsertReschedulePickup(input:${'$'}input){
                    status
                    message
                    errors
                }
            }
        """
    }
}
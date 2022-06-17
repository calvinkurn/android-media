package com.tokopedia.logisticseller.reschedulepickup.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.reschedulepickup.data.model.SaveReschedulePickupParam
import com.tokopedia.logisticseller.reschedulepickup.data.model.SaveReschedulePickupResponse
import javax.inject.Inject

@GqlQuery(
    SaveReschedulePickupUseCase.SaveReschedulePickupMutation,
    SaveReschedulePickupUseCase.MP_LOGISTIC_INSERT_RESCHEDULE_PICKUP
)
class SaveReschedulePickupUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SaveReschedulePickupParam, SaveReschedulePickupResponse.Data>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return MP_LOGISTIC_INSERT_RESCHEDULE_PICKUP
    }

    override suspend fun execute(params: SaveReschedulePickupParam): SaveReschedulePickupResponse.Data {
        val param = mapOf(LogisticSellerConst.PARAM_INPUT to params)
        return repository.request(SaveReschedulePickupMutation(), param)
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
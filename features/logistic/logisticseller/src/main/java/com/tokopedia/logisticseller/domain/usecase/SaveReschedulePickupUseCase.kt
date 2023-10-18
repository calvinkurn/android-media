package com.tokopedia.logisticseller.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.data.param.SaveReschedulePickupParam
import com.tokopedia.logisticseller.data.response.SaveReschedulePickupResponse
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
        return repository.request(SaveReschedulePickupMutation(), params)
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

package com.tokopedia.logisticseller.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.data.param.GetReschedulePickupParam
import com.tokopedia.logisticseller.data.response.GetReschedulePickupResponse
import javax.inject.Inject

@GqlQuery(
    GetReschedulePickupUseCase.GetReschedulePickupQuery,
    GetReschedulePickupUseCase.MP_LOGISTIC_GET_RESCHEDULE_PICKUP
)
class GetReschedulePickupUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetReschedulePickupParam, GetReschedulePickupResponse.Data>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return MP_LOGISTIC_GET_RESCHEDULE_PICKUP
    }

    override suspend fun execute(params: GetReschedulePickupParam): GetReschedulePickupResponse.Data {
        return repository.request(GetReschedulePickupQuery(), params)
    }

    companion object {
        const val GetReschedulePickupQuery = "GetReschedulePickupQuery"
        const val MP_LOGISTIC_GET_RESCHEDULE_PICKUP = """
            query GetReschedulePickup(${'$'}input:MpLogisticGetReschedulePickupInputs!){
                mpLogisticGetReschedulePickup(input:${'$'}input) {
                    app_link
                    order_detail_ticker
                    data{
                        shipper_id
                        shipper_name
      	                order_data{
                            order_id
        	                invoice
        	                shipper_product_id
                            shipper_product_name
        	                order_item{
                                name
                                qty
        	                }
                            choose_day{
                                day
                                choose_time{
                                    time
                                    eta_pickup
                                }
                            }
        	                choose_reason{
          	                    reason
        	                }
        	                error_message
    	                }
                    }
                }
            }
        """
    }
}

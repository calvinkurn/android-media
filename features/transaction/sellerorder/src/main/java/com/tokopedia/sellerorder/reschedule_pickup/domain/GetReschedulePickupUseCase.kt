package com.tokopedia.sellerorder.reschedule_pickup.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import javax.inject.Inject

class GetReschedulePickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<GetReschedulePickupResponse.Data>) {

    init {
        useCase.setTypeClass(GetReschedulePickupResponse.Data::class.java)
    }

    @GqlQuery(GetReschedulePickupQuery, MP_LOGISTIC_GET_RESCHEDULE_PICKUP)
    suspend fun execute(param: GetReschedulePickupParam): GetReschedulePickupResponse.Data {
        useCase.setGraphqlQuery(GetReschedulePickupQuery())
        useCase.setRequestParams(generateParam(param))
        return useCase.executeOnBackground()
    }

    private fun generateParam(param: GetReschedulePickupParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }

    companion object {
        const val GetReschedulePickupQuery = "GetReschedulePickupQuery"
        const val MP_LOGISTIC_GET_RESCHEDULE_PICKUP = """
            query GetReschedulePickup(${'$'}input:MpLogisticGetReschedulePickupInputs!){
                mpLogisticGetReschedulePickup(input:${'$'}input) {
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
package com.tokopedia.sellerorder.reschedule_pickup.domain

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetReschedulePickupQuery : GqlQueryInterface {
    override fun getOperationNameList() = listOf(
        "GetReschedulePickup"
    )

    override fun getQuery(): String {
        return """
            query GetReschedulePickup(${'$'}input:MpLogisticGetReschedulePickupInputs!){
                mpLogisticGetReschedulePickup(input:${'$'}input) {
                    data{
                        shipper_id
      	                order_data{
                            order_id
        	                invoice
        	                shipper_product_id
        	                order_item{
                                name
                                qty
        	                }
                            choose_day{
                                day
                                choose_time{
                                    time
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
        """.trimIndent()
    }

    override fun getTopOperationName() = "GetReschedulePickup"
}
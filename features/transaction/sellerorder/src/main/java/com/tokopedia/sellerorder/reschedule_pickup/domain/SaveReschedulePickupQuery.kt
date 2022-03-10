package com.tokopedia.sellerorder.reschedule_pickup.domain

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object SaveReschedulePickupQuery : GqlQueryInterface {
    override fun getOperationNameList() = listOf(
        "SaveReschedulePickup"
    )

    override fun getQuery(): String {
        return """
            mutation saveReschedulePickup(${'$'}input:MpLogisticInsertReschedulePickupInputs!){
                mpLogisticInsertReschedulePickup(input:${'$'}input){
                    status
                    message
                }
            }
        """.trimIndent()
    }

    override fun getTopOperationName() = "SaveReschedulePickup"
}
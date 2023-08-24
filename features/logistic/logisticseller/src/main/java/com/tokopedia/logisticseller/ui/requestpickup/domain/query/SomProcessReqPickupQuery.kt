package com.tokopedia.logisticseller.ui.requestpickup.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object SomProcessReqPickupQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "RequestPickup"
    private val SOM_CONFIRM_REQ_QUERY = """
        mutation $OPERATION_NAME(${'$'}input: MPLogisticRequestPickupInputs!) {
              mpLogisticRequestPickup(input: ${'$'}input){
                message
              }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = SOM_CONFIRM_REQ_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}

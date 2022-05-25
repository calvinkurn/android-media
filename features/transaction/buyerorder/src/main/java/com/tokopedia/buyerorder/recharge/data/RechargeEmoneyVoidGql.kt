package com.tokopedia.buyerorder.recharge.data

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class RechargeEmoneyVoidGql : GqlQueryInterface {
    override fun getOperationNameList(): List<String> =
        listOf("rechargeEmoneyVoid")

    override fun getQuery(): String = """
        query rechargeEmoneyVoid(${'$'}orderID:String!) {
          rechargeEmoneyVoid(orderID:${'$'}orderID) {
            status
            message
          }
        }
        """.trimIndent()

    override fun getTopOperationName(): String = "rechargeEmoneyVoid"
}
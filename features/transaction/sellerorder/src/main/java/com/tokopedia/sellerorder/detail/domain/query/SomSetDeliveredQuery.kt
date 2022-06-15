package com.tokopedia.sellerorder.detail.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object SomSetDeliveredQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "setDelivered"
    private val SET_DELIVERY_QUERY = """
            mutation ${OPERATION_NAME}(${'$'}input:SetDeliveredRequest!) {
              set_delivered(input: ${'$'}input) {
                success
                message
              }
            }
        """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = SET_DELIVERY_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}
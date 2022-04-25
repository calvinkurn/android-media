package com.tokopedia.tokofood.feature.ordertracking.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

object DriverPhoneNumberQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "GetDriverPhoneNumber"

    private val DRIVER_PHONE_NUMBER_QUERY = """
        query GetDriverPhoneNumber(${'$'}orderID: String!) {
          tokofoodDriverPhoneNumber(orderID: ${'$'}orderID) {
            isCallable
            phoneNumber
          }
        }
    """.trimIndent()

    private const val ORDER_ID_KEY = "orderID"

    fun createRequestParams(orderId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(ORDER_ID_KEY, orderId)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = DRIVER_PHONE_NUMBER_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME


}
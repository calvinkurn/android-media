package com.tokopedia.logisticseller.ui.confirmshipping.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

object GetConfirmShippingQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "mpLogisticConfirmShipping"
    private val GET_CONFIRM_SHIPPING_QUERY = """
            mutation $OPERATION_NAME(${'$'}orderId: String!, ${'$'}shippingRef: String!){
                $OPERATION_NAME(input: {
                                  order_id: ${'$'}orderId,
                                  shipping_ref: ${'$'}shippingRef,
                                  is_active_saldo_prioritas:false}) {
                    message
                }
            }
        """.trimIndent()

    private const val ORDER_ID_KEY = "orderId"
    private const val SHIPPING_REF_KEY = "shippingRef"

    fun createParamGetConfirmShipping(orderId: String, shippingRef: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(ORDER_ID_KEY, orderId)
            putString(SHIPPING_REF_KEY, shippingRef)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GET_CONFIRM_SHIPPING_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}
